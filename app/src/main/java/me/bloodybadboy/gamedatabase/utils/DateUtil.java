package me.bloodybadboy.gamedatabase.utils;

import android.text.format.DateUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DateUtil {
  private static final SimpleDateFormat targetDateFormat =
      new SimpleDateFormat("MMMM dd, YYYY", Locale.ENGLISH);
  private static GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

  public static String getHumanReadableDate(long timeInMillis) {
    return new SimpleDateFormat("MMMM dd, YYYY", Locale.getDefault()).format(
        new Date(timeInMillis));
  }

  public static String getPrettyDate(long timeInMillis) {
    if (timeInMillis > 0) {

      Date date = new Date(timeInMillis);
      if (!(date.before(START_OF_EPOCH.getTime()))) {
        return DateUtils.getRelativeTimeSpanString(
            date.getTime(),
            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_ALL).toString();
      } else {
        return targetDateFormat.format(date);
      }
    }
    return "N/A";
  }
}
