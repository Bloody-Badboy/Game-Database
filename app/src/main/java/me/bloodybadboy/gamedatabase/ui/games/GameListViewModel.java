package me.bloodybadboy.gamedatabase.ui.games;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.Constants;
import me.bloodybadboy.gamedatabase.Constants.GameListFilterType;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.domain.usecase.AbstractGetGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetComingSoonGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetFavouriteGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetPopularGameListUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetTopRatedGameListUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.event.Event;
import timber.log.Timber;

public class GameListViewModel extends ViewModel {

  public final LiveData<Boolean> showProgress;
  public final LiveData<Boolean> emptyGameList;

  final LiveData<Result<List<Game>>> gameListResult;
  final LiveData<Boolean> gameListLoading;
  final LiveData<Event<Boolean>> swapGameList;
  final LiveData<GameListFilterType> listFilterType;
  final MutableLiveData<Event<String>> showRetrySnackBarEvent;

  private final MutableLiveData<GameListFilterType> _listFilterType = new MutableLiveData<>();
  private final MutableLiveData<Result<List<Game>>> _gameListResult = new MutableLiveData<>();
  private final MutableLiveData<Event<Boolean>> _swapGameList = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _emptyGameList = new MutableLiveData<>();
  private final MutableLiveData<Event<String>> _showRetrySnackBarEvent = new MutableLiveData<>();

  private final List<Game> games = new ArrayList<>();
  private final GetPopularGameListUseCase popularGameListUseCase;
  private final GetTopRatedGameListUseCase topRatedGameListUseCase;
  private final GetComingSoonGameListUseCase comingSoonGameListUseCase;
  private final GetFavouriteGameListUseCase favouriteGameListUseCase;
  private GameListFilterType gameListFilterType = GameListFilterType.POPULARITY;
  private boolean isFilterTypeChanged;
  private int paginationOffset = 0;
  private boolean isProgressBarShown;
  private AbstractGetGameListUseCase currentGameListUseCase;

  public GameListViewModel(
      GetPopularGameListUseCase popularGameListUseCase,
      GetTopRatedGameListUseCase topRatedGameListUseCase,
      GetComingSoonGameListUseCase comingSoonGameListUseCase,
      GetFavouriteGameListUseCase favouriteGameListUseCase
  ) {
    this.popularGameListUseCase = popularGameListUseCase;
    this.topRatedGameListUseCase = topRatedGameListUseCase;
    this.comingSoonGameListUseCase = comingSoonGameListUseCase;
    this.favouriteGameListUseCase = favouriteGameListUseCase;

    showProgress = _showProgress;
    emptyGameList = _emptyGameList;
    listFilterType = _listFilterType;
    swapGameList = _swapGameList;
    showRetrySnackBarEvent = _showRetrySnackBarEvent;

    _listFilterType.setValue(gameListFilterType);

    gameListLoading = Transformations.map(_gameListResult, Result::loading);
    gameListResult = Transformations.map(_gameListResult, input -> {
      if (input.loading()) {
        return Result.Loading();
      } else if (input.succeeded()) {
        if (isFilterTypeChanged) {
          isFilterTypeChanged = false;
          games.clear();
        }
        games.addAll(input.data);
        hideProgressBar();
        if (games.size() > 0) {
          _emptyGameList.setValue(false);
        } else {
          _emptyGameList.setValue(true);
        }
        return Result.Success(games);
      } else {
        hideProgressBar();
        return Result.Error(input.exception);
      }
    });

    requestForGameList(gameListFilterType);
  }

  void setGameListFilterType(GameListFilterType filterType) {
    _listFilterType.setValue(filterType);

    if (gameListFilterType != filterType) {
      Timber.d("GameListFilterType changed! current GameListFilterType: %s", filterType);
      gameListFilterType = filterType;
      isFilterTypeChanged = true;
    }

    if (isFilterTypeChanged) {
      requestForGameList(filterType);
      _swapGameList.setValue(new Event<>(true));
      paginationOffset = 0;
    } else {
      _swapGameList.setValue(new Event<>(false));
    }
  }

  void requestForPaginationGameList() {
    if (currentGameListUseCase == null) {
      Timber.e("currentGameListUseCase == null");
      return;
    }
    paginationOffset += Constants.GAME_LIST_LIMIT;
    currentGameListUseCase.setPaginationOffset(paginationOffset);
    currentGameListUseCase.executeAsync(_gameListResult);

    Timber.d("requestForPaginationGameList()  paginationOffset: %s", paginationOffset);
  }

  private void requestForGameList(GameListFilterType filterType) {
    showProgressBar();
    if (filterType == GameListFilterType.POPULARITY) {
      currentGameListUseCase = popularGameListUseCase;
      popularGameListUseCase.executeAsync(_gameListResult);
    } else if (filterType == GameListFilterType.TOP_RATED) {
      currentGameListUseCase = topRatedGameListUseCase;
      topRatedGameListUseCase.executeAsync(_gameListResult);
    } else if (filterType == GameListFilterType.COMING_SOON) {
      currentGameListUseCase = comingSoonGameListUseCase;
      comingSoonGameListUseCase.executeAsync(_gameListResult);
    } else if (filterType == GameListFilterType.FAVOURITES) {
      currentGameListUseCase = null;
      favouriteGameListUseCase.executeAsync(_gameListResult);
    } else {
      throw new IllegalArgumentException("Unknown GameListFilterType: " + filterType);
    }
  }

  private void showProgressBar() {
    if (!isProgressBarShown) {
      isProgressBarShown = true;
      _showProgress.setValue(true);
    }
  }

  private void hideProgressBar() {
    if (isProgressBarShown) {
      isProgressBarShown = false;
      _showProgress.setValue(false);
    }
  }

  void showRetrySnackBar(String message) {
    _showRetrySnackBarEvent.setValue(new Event<>(message));
  }

  void onClickRetry() {
    currentGameListUseCase.executeAsync(_gameListResult);
  }

  void gameRemovedFromFavourites(int adapterPosition) {
    List<Game> newGameList = new ArrayList<>(games);
    newGameList.remove(adapterPosition);
    games.clear();
    _gameListResult.setValue(Result.Success(newGameList));
  }
}
