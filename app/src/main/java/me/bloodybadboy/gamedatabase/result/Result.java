package me.bloodybadboy.gamedatabase.result;

import androidx.annotation.NonNull;

public abstract class Result<T> {

  public T data;

  public Exception exception;

  public static <T> Result<T> Error(Exception exception) {
    return new Error<>(exception);
  }

  public static <T> Result<T> Success(T data) {
    return new Success<>(data);
  }

  public static <T> Result<T> Loading() {
    return new Loading<>();
  }

  public boolean succeeded() {
    if (this instanceof Success) {
      return ((Success) this).data != null;
    }
    return false;
  }

  public boolean loading() {
    return this instanceof Loading;
  }

  @NonNull @Override public String toString() {
    if (this instanceof Success) {
      return "Success[data=" + ((Success) this).data + "]";
    } else if (this instanceof Error) {
      return "Error[exception=" + ((Error) this).exception + "]";
    } else {
      return "Loading";
    }
  }

  private static final class Success<T> extends Result<T> {

    Success(T data) {
      this.data = data;
    }
  }

  private static final class Error<T> extends Result<T> {

    Error(Exception exception) {
      this.exception = exception;
    }
  }

  private static final class Loading<T> extends Result<T> {

  }
}
