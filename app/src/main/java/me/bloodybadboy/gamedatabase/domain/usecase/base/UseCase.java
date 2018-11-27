package me.bloodybadboy.gamedatabase.domain.usecase.base;

import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public abstract class UseCase<T> {

  private final Scheduler scheduler;
  private OnResponseListener<T> listener;

  protected UseCase(
      Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public UseCase<T> setListener(OnResponseListener<T> listener) {
    this.listener = listener;
    return this;
  }

  /**
   * Executes the use case asynchronously
   */
  public void executeAsync() {
    scheduler.postToMainThread(() -> {
      if (listener != null) {
        listener.onResponse(Result.Loading());
      }
    });
    scheduler.execute(() -> {
      try {
        final Result<T> result = execute();
        scheduler.postToMainThread(() -> {
          if (listener != null) {
            listener.onResponse(result);
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
        scheduler.postToMainThread(() -> {
          if (listener != null) {
            listener.onResponse(Result.Error(e));
          }
        });
      }
    });
  }

  /**
   * Executes the use case synchronously
   */
  public void executeNow() {
    try {
      final Result<T> result = execute();
      listener.onResponse(result);
    } catch (Exception e) {
      e.printStackTrace();
      listener.onResponse(Result.Error(e));
    }
  }

  protected abstract Result<T> execute() throws Exception;

  public interface OnResponseListener<T> {
    void onResponse(Result<T> result);
  }
}
