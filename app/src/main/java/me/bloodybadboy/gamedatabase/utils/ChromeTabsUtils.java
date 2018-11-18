package me.bloodybadboy.gamedatabase.utils;

import android.content.Context;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsIntent;
import me.bloodybadboy.gamedatabase.R;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public class ChromeTabsUtils {
  public static void open(Context context, String url) {
    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .setToolbarColor(context.getResources().getColor(R.color.color_primary))
        .setShowTitle(true)
        .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
        .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
        .build();

    CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent);

    CustomTabsHelper.openCustomTab(
        context, customTabsIntent,
        Uri.parse(url),
        new WebViewFallback());
  }
}
