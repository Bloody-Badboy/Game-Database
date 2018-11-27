package me.bloodybadboy.gamedatabase.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import java.util.List;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.domain.usecase.SearchGameUseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.event.Event;
import timber.log.Timber;

public class GameSearchViewModel extends ViewModel {

  public final LiveData<Boolean> loading;
  public final LiveData<Boolean> noResultsFound;
  final LiveData<List<Game>> gameList;
  final LiveData<Event<Exception>> exceptionEvent;
  final LiveData<Event<String>> showRetrySnackBarEvent;

  private final MutableLiveData<Result<List<Game>>> observableGameList = new MutableLiveData<>();
  private final MutableLiveData<List<Game>> _gameList = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _noResultsFound = new MutableLiveData<>();
  private final MutableLiveData<Event<Exception>> _exceptionEvent = new MutableLiveData<>();
  private final MutableLiveData<Event<String>> _showRetrySnackBarEvent = new MutableLiveData<>();

  private SearchGameUseCase searchGameUseCase;

  @Inject GameSearchViewModel(
      SearchGameUseCase searchGameUseCase
  ) {
    this.searchGameUseCase = searchGameUseCase;

    noResultsFound = _noResultsFound;
    gameList = _gameList;
    exceptionEvent = _exceptionEvent;
    showRetrySnackBarEvent = _showRetrySnackBarEvent;

    loading = Transformations.map(observableGameList, input -> {
      if (!input.loading()) {
        if (input.succeeded()) {
          List<Game> games = input.data;
          if (games.size() > 0) {
            _gameList.setValue(input.data);
            _noResultsFound.setValue(false);
          } else {
            _noResultsFound.setValue(true);
          }
        } else {
          Timber.e(input.exception);
          _noResultsFound.setValue(true);
          _exceptionEvent.setValue(new Event<>(input.exception));
        }
      }
      return input.loading();
    });
  }

  void searchForGame(String search) {
    searchGameUseCase.setSearchQuery(search);
    searchGameUseCase.executeAsync(observableGameList);
  }

  void showRetrySnackBar(String message) {
    _showRetrySnackBarEvent.setValue(new Event<>(message));
  }

  void onClickRetry() {
    searchGameUseCase.executeAsync(observableGameList);
  }
}
