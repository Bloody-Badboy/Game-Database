package me.bloodybadboy.gamedatabase.utils.scheduler;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class AsyncScheduler implements Scheduler {
  private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
  private static final int KEEP_ALIVE_TIME = 1;
  private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
  private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
  private final ExecutorService executorService = new ThreadPoolExecutor(
      NUMBER_OF_CORES,       // Initial pool size
      NUMBER_OF_CORES,       // Max pool size
      KEEP_ALIVE_TIME,
      KEEP_ALIVE_TIME_UNIT,
      workQueue);

  @Override public void execute(Runnable task) {
    executorService.execute(task);
  }

  @Override public void postToMainThread(Runnable task) {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      task.run();
    } else {
      Handler mainThreadHandler = new Handler(Looper.getMainLooper());
      mainThreadHandler.post(task);
    }
  }
}
