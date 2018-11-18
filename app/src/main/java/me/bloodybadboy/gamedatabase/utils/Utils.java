package me.bloodybadboy.gamedatabase.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.Constants;
import me.bloodybadboy.gamedatabase.data.model.Company;
import me.bloodybadboy.gamedatabase.data.model.Theme;

public final class Utils {

  private static final DecimalFormat ratingFormat = new DecimalFormat("0.0");

  public static String getDisplayableRating(double rating) {
    if (rating <= 0) {
      return "N/A";
    }
    return ratingFormat.format(rating / 10.0);
  }

  public static float getFormattedRating(double rating) {
    if (rating <= 0) {
      return 0;
    }
    return Float.parseFloat(ratingFormat.format(rating / 20.0));
  }

  public static String getDisplayableThemeList(@Nullable List<Theme> themes) {
    if (themes == null || themes.size() <= 0) {
      return "N/A";
    }
    List<String> displayableThemes = new ArrayList<>();
    for (Theme theme : themes) {
      if (!TextUtils.isEmpty(theme.getName())) {
        displayableThemes.add(theme.getName());
      }
    }
    return TextUtils.join(" | ", displayableThemes);
  }

  public static String getDisplayableCompanyNameList(@Nullable List<Company> companies) {
    if (companies == null || companies.size() <= 0) {
      return "N/A";
    }
    List<String> displayableCompanies = new ArrayList<>();
    for (Company company : companies) {
      if (!TextUtils.isEmpty(company.getName())) {
        displayableCompanies.add(company.getName());
      }
    }
    return TextUtils.join("\n", displayableCompanies);
  }

  @Nullable public static String getYouTubeThumbnailUrl(@Nullable String videoId) {
    if (videoId == null || TextUtils.isEmpty(videoId)) return null;
    return Constants.YOUTUBE_THUMBNAIL_BASE_URL
        + videoId
        + "/"
        + Constants.YOUTUBE_THUMBNAIL_IMAGE_SIZE;
  }

  public static String naIfNullOrEmpty(String string) {
    if (TextUtils.isEmpty(string)) {
      return "N/A";
    } else {
      return string;
    }
  }

  public static boolean canPerformIntent(@NonNull Context context, @NonNull Intent intent) {
    PackageManager packageManager = context.getPackageManager();
    List<ResolveInfo> resolveInfos =
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    return resolveInfos.size() > 0;
  }
}
