package me.bloodybadboy.gamedatabase.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;

public final class ViewUtils {
  public ViewUtils() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static void doOnPreDraw(@NonNull final View view, @NonNull final Runnable runnable) {
    final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
    viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override public boolean onPreDraw() {
        runnable.run();
        if (viewTreeObserver.isAlive()) {
          viewTreeObserver.removeOnPreDrawListener(this);
        } else {
          view.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        return true;
      }
    });
  }
}
