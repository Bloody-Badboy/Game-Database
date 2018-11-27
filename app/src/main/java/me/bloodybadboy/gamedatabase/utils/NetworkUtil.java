package me.bloodybadboy.gamedatabase.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import me.bloodybadboy.gamedatabase.GameDatabaseApplication;

public final class NetworkUtil {

  private NetworkUtil() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static boolean isOnline() {

    ConnectivityManager connectivityManager =
        (ConnectivityManager) GameDatabaseApplication.getInstance()
            .getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivityManager != null) {
      NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
      return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    return false;
  }
}
