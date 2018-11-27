package me.bloodybadboy.gamedatabase.ui.games;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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

  final LiveData<List<Object>> objectListResult;
  final LiveData<Boolean> gameListLoading;
  final LiveData<Event<Boolean>> swapGameList;
  final LiveData<GameListFilterType> listFilterType;
  final LiveData<Event<Exception>> exceptionEvent;
  final LiveData<Event<String>> showRetrySnackBarEvent;
  final LiveData<Event<Object>> loadMoreNativeAds;

  private final MutableLiveData<GameListFilterType> _listFilterType = new MutableLiveData<>();
  private final MutableLiveData<Result<List<Game>>> _gameListResult = new MutableLiveData<>();
  private final MutableLiveData<Result<List<Object>>> _objectListResult = new MutableLiveData<>();
  private final MutableLiveData<Event<Boolean>> _swapGameList = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _emptyGameList = new MutableLiveData<>();
  private final MutableLiveData<Event<Exception>> _exceptionEvent = new MutableLiveData<>();
  private final MutableLiveData<Event<String>> _showRetrySnackBarEvent = new MutableLiveData<>();
  private final MutableLiveData<Event<Object>> _loadMoreNativeAds = new MutableLiveData<>();

  private final List<Object> items = new ArrayList<>();
  private final List<UnifiedNativeAd> nativeAds = new ArrayList<>();
  private final GetPopularGameListUseCase popularGameListUseCase;
  private final GetTopRatedGameListUseCase topRatedGameListUseCase;
  private final GetComingSoonGameListUseCase comingSoonGameListUseCase;
  private final GetFavouriteGameListUseCase favouriteGameListUseCase;
  private AbstractGetGameListUseCase currentGameListUseCase;
  private GameListFilterType gameListFilterType = GameListFilterType.POPULARITY;
  private boolean isFilterTypeChanged;
  private int paginationOffset = 0;
  private boolean isProgressBarShown;
  private int currentNativeAdIndex = 0;

  @Inject GameListViewModel(
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
    exceptionEvent = _exceptionEvent;
    showRetrySnackBarEvent = _showRetrySnackBarEvent;
    loadMoreNativeAds = _loadMoreNativeAds;

    _listFilterType.setValue(gameListFilterType);

    gameListLoading = Transformations.map(_gameListResult, Result::loading);

    final MediatorLiveData<List<Object>> result = new MediatorLiveData<>();
    result.addSource(_gameListResult, input -> {
      if (!input.loading()) {
        if (input.succeeded()) {
          if (isFilterTypeChanged) {
            isFilterTypeChanged = false;
            items.clear();
          }
          items.addAll(input.data);
          hideProgressBar();
          if (items.size() > 0) {
            _emptyGameList.setValue(false);
          } else {
            _emptyGameList.setValue(true);
          }

          if (gameListFilterType != GameListFilterType.FAVOURITES) {
            // Insert a native ad after each 40 items (if available)
            int nativeAdsSize = nativeAds.size();
            if (paginationOffset % 4 == 0) {
              if (nativeAdsSize > 0) {
                items.add(nativeAds.get(currentNativeAdIndex));
                currentNativeAdIndex++;

                Timber.d("Inserted UnifiedNativeAd in the list.");

                if (currentNativeAdIndex >= nativeAdsSize) {
                  currentNativeAdIndex = 0;
                  _loadMoreNativeAds.setValue(new Event<>(new Object()));
                }
              }
            }
          }

          result.setValue(items);
        } else {
          hideProgressBar();
          _exceptionEvent.setValue(new Event<>(input.exception));
        }
      }
    });
    result.addSource(_objectListResult, listResult -> {
      if (listResult.succeeded()) {
        result.setValue(listResult.data);
      }
    });

    objectListResult = result;

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
    List<Object> newGameList = new ArrayList<>(items);
    newGameList.remove(adapterPosition);
    items.clear();
    _objectListResult.setValue(Result.Success(newGameList));
  }

  void setNativeAdList(List<UnifiedNativeAd> nativeAds) {
    this.nativeAds.clear();
    this.nativeAds.addAll(nativeAds);
  }
}
