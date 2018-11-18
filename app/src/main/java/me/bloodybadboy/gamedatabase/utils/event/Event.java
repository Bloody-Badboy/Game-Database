package me.bloodybadboy.gamedatabase.utils.event;

public final class Event<T> {
  private final T content;
  private boolean hasBeenHandled = false;

  public Event(T content) {
    this.content = content;
  }

  T getContentIfNotHandled() {
    if (hasBeenHandled) {
      return null;
    } else {
      hasBeenHandled = true;
      return content;
    }
  }

  public T peek() {
    return content;
  }
}
