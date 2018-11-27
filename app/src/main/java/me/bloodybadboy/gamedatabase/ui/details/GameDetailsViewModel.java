package me.bloodybadboy.gamedatabase.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.Constants;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Image;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.domain.usecase.AddGameToFavouritesUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.AddNewsToDatabaseUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.CheckGameInFavouritesUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetGameDetailsUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.GetGameNewsByTagUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.RemoveGameFromFavouritesUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.RemoveNewsFromDatabaseUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.event.Event;
import timber.log.Timber;

public class GameDetailsViewModel extends ViewModel {

  public final LiveData<Boolean> detailsLoading;
  public final LiveData<Boolean> newsLoading;
  public final LiveData<Boolean> hasNoNews;

  final LiveData<Boolean> isFavourite;
  final LiveData<Game> game;
  final LiveData<Image> bannerImage;
  final LiveData<List<Pulse>> pulses;
  final LiveData<Event<Exception>> exceptionEvent;
  final LiveData<Event<String>> showRetrySnackBarEvent;
  final LiveData<Event<Object>> shareGameEvent;

  private final MutableLiveData<Result<Game>> _gameResult = new MutableLiveData<>();
  private final MutableLiveData<Game> _game = new MutableLiveData<>();
  private final MutableLiveData<Image> _bannerImage = new MutableLiveData<>();
  private final MutableLiveData<Result<List<Pulse>>> _pulsesResult = new MutableLiveData<>();
  private final MutableLiveData<List<Pulse>> _pulses = new MutableLiveData<>();
  private final MutableLiveData<Result<Boolean>> _isFavouriteResult = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _hasNoNews = new MutableLiveData<>();
  private final MutableLiveData<Event<Exception>> _exceptionEvent = new MutableLiveData<>();
  private final MutableLiveData<Event<String>> _showRetrySnackBarEvent = new MutableLiveData<>();
  private final MutableLiveData<Event<Object>> _shareGameEvent = new MutableLiveData<>();

  private GetGameDetailsUseCase getGameDetailsUseCase;
  private GetGameNewsByTagUseCase getGameNewsByTagUseCase;
  private AddGameToFavouritesUseCase addGameToFavouritesUseCase;
  private RemoveGameFromFavouritesUseCase removeGameFromFavouritesUseCase;
  private CheckGameInFavouritesUseCase checkGameInFavouritesUseCase;
  private AddNewsToDatabaseUseCase addNewsToDatabaseUseCase;
  private RemoveNewsFromDatabaseUseCase removeNewsFromDatabaseUseCase;

  private List<Pulse> offlinePulses = new ArrayList<>();
  private boolean isGameInFavourite;

  @Inject
  public GameDetailsViewModel(
      GetGameDetailsUseCase getGameDetailsUseCase,
      GetGameNewsByTagUseCase getGameNewsByTagUseCase,
      AddGameToFavouritesUseCase addGameToFavouritesUseCase,
      RemoveGameFromFavouritesUseCase removeGameFromFavouritesUseCase,
      CheckGameInFavouritesUseCase checkGameInFavouritesUseCase,
      AddNewsToDatabaseUseCase addNewsToDatabaseUseCase,
      RemoveNewsFromDatabaseUseCase removeNewsFromDatabaseUseCase) {

    this.getGameDetailsUseCase = getGameDetailsUseCase;
    this.getGameNewsByTagUseCase = getGameNewsByTagUseCase;
    this.addGameToFavouritesUseCase = addGameToFavouritesUseCase;
    this.removeGameFromFavouritesUseCase = removeGameFromFavouritesUseCase;
    this.checkGameInFavouritesUseCase = checkGameInFavouritesUseCase;
    this.addNewsToDatabaseUseCase = addNewsToDatabaseUseCase;
    this.removeNewsFromDatabaseUseCase = removeNewsFromDatabaseUseCase;

    game = _game;
    bannerImage = _bannerImage;
    pulses = _pulses;
    hasNoNews = _hasNoNews;
    exceptionEvent = _exceptionEvent;
    showRetrySnackBarEvent = _showRetrySnackBarEvent;
    shareGameEvent = _shareGameEvent;

    detailsLoading = Transformations.map(_gameResult, input -> {
      if (!input.loading()) {
        if (input.succeeded()) {
          _game.setValue(input.data);
          List<Image> images = input.data.getScreenshots();
          if (images != null && images.size() > 0) {
            int randomIndex = new Random().nextInt(images.size());
            _bannerImage.setValue(images.get(randomIndex));
          }
        } else {
          _exceptionEvent.setValue(new Event<>(input.exception));
        }
      }
      return input.loading();
    });

    newsLoading = Transformations.map(_pulsesResult, input -> {
      if (!input.loading()) {
        if (input.succeeded()) {
          List<Pulse> pulseList = input.data;
          if (pulseList.size() > 0) {
            _pulses.setValue(pulseList);
            _hasNoNews.setValue(false);

            if (pulseList.size() > Constants.OFFLINE_PULSE_LIST_LIMIT) {
              for (int i = 0; i < Constants.OFFLINE_PULSE_LIST_LIMIT; i++) {
                offlinePulses.add(pulseList.get(i));
              }
            } else {
              offlinePulses.addAll(pulseList);
            }
          } else {
            _hasNoNews.setValue(true);
          }
        } else {
          Timber.d(input.exception);
          _hasNoNews.setValue(true);
        }
      }
      return input.loading();
    });

    isFavourite = Transformations.map(_isFavouriteResult, input -> {
      if (input.succeeded()) {
        isGameInFavourite = input.data;
        return isGameInFavourite;
      }
      return null;
    });
  }

  void requestForGameDetails(long gameId) {
    getGameDetailsUseCase.setGameId(gameId).executeAsync(_gameResult);
  }

  void requestForGameNews(int tagNumber) {
    getGameNewsByTagUseCase.setGameTagNumber(tagNumber).executeAsync(_pulsesResult);
  }

  void checkIsGameInFavourite(long gameId) {
    checkGameInFavouritesUseCase.setGame(gameId).executeAsync(_isFavouriteResult);
  }

  private void addGameToFavourites(Game game) {
    Timber.d("addGameToFavourites() gameId: %s", game.getId());
    addGameToFavouritesUseCase.setGame(game).executeAsync();
    if (offlinePulses.size() > 0) {
      addNewsToDatabaseUseCase.setPulseList(offlinePulses).executeAsync();
    }
    isGameInFavourite = true;
    _isFavouriteResult.setValue(Result.Success(true));
  }

  private void removeGameFromFavourites(long gameId) {
    Timber.d("removeGameFromFavourites() gameId: %s", gameId);
    removeGameFromFavouritesUseCase.setGame(gameId).executeAsync();
    removeNewsFromDatabaseUseCase.setPulseList(offlinePulses).executeAsync();
    isGameInFavourite = false;
    _isFavouriteResult.setValue(Result.Success(false));
  }

  public void handleFabClick(Game game) {
    if (game == null) {
      Timber.d("game == null");
      return;
    }
    if (isGameInFavourite) {
      removeGameFromFavourites(game.getId());
    } else {
      addGameToFavourites(game);
    }
  }

  void showRetrySnackBar(String message) {
    _showRetrySnackBarEvent.setValue(new Event<>(message));
  }

  void onClickRetry() {
    getGameDetailsUseCase.executeAsync(_gameResult);
    getGameNewsByTagUseCase.executeAsync(_pulsesResult);
  }

  void onClickShareGame() {
    _shareGameEvent.setValue(new Event<>(new Object()));
  }
}
