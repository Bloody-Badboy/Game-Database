package me.bloodybadboy.gamedatabase.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import me.bloodybadboy.gamedatabase.Constants;
import me.bloodybadboy.gamedatabase.R;

/**
 * Implementation of App Widget functionality.
 */
public class GameNewsAppWidget extends AppWidgetProvider {

  public static String CLICK_ACTION = Constants.PACKAGE + ".CLICK";
  public static String EXTRA_NEWS_URL = Constants.PACKAGE + ".URL";

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.game_news_app_widget);

    final Intent clickIntent = new Intent(context, GameNewsAppWidget.class);
    clickIntent.setAction(CLICK_ACTION);
    clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    Uri uri = Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME));
    clickIntent.setData(uri);
    final PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, appWidgetId,
        clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    views.setPendingIntentTemplate(R.id.lv_news, clickPendingIntent);

    Intent intent = new Intent(context, GameNewsWidgetService.class);
    views.setRemoteAdapter(R.id.lv_news, intent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (action != null && action.equals(CLICK_ACTION) && intent.hasExtra(EXTRA_NEWS_URL)) {
      String url = intent.getStringExtra(EXTRA_NEWS_URL);
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.getApplicationContext().startActivity(browserIntent);
    }
    super.onReceive(context, intent);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

