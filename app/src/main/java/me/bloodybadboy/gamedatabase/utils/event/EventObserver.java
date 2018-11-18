package me.bloodybadboy.gamedatabase.utils.event;

import androidx.lifecycle.Observer;

public class EventObserver<T> implements Observer<Event<T>> {

  private UnhandledContentObserver<T> observer;

  public EventObserver(UnhandledContentObserver<T> observer) {
    this.observer = observer;
  }

  @Override public void onChanged(Event<T> tEvent) {
    if (tEvent != null) {
      T content = tEvent.getContentIfNotHandled();
      if (content != null) {
        if (observer != null) {
          observer.onEventUnhandledContent(content);
        }
      }
    }
  }

  public interface UnhandledContentObserver<T> {
    void onEventUnhandledContent(T t);
  }
}
