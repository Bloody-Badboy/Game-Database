package me.bloodybadboy.gamedatabase.utils.scheduler;

public interface Scheduler {
  void execute(Runnable task);

  void postToMainThread(Runnable task);
}
