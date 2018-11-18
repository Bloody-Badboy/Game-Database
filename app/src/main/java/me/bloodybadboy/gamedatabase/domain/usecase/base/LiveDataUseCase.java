package me.bloodybadboy.gamedatabase.domain.usecase.base;

import androidx.lifecycle.MutableLiveData;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public abstract class LiveDataUseCase<T> {
  private final Scheduler scheduler;

  public LiveDataUseCase(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * Executes the use case asynchronously
   */
  public void executeAsync(final MutableLiveData<Result<T>> resultMutableLiveData) {
    resultMutableLiveData.postValue(Result.Loading());
    scheduler.execute(() -> {
      try {
        resultMutableLiveData.postValue(execute());
      } catch (Exception e) {
        e.printStackTrace();
        resultMutableLiveData.postValue(Result.Error(e));
      }
    });
  }

  /**
   * Executes the use case synchronously
   */
  public MutableLiveData<Result<T>> executeAsync() {
    final MutableLiveData<Result<T>> resultMutableLiveData = new MutableLiveData<>();
    executeAsync(resultMutableLiveData);
    return resultMutableLiveData;
  }

  protected abstract Result<T> execute() throws Exception;
}
