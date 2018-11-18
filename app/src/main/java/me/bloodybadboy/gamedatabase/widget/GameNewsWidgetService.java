package me.bloodybadboy.gamedatabase.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Image;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.domain.usecase.GetFavouriteGamesNewsUseCase;
import me.bloodybadboy.gamedatabase.injection.Injection;
import me.bloodybadboy.gamedatabase.utils.DateUtil;
import timber.log.Timber;

import static me.bloodybadboy.gamedatabase.Constants.IMAGE_THUMB;
import static me.bloodybadboy.gamedatabase.Constants.IMAGE_URL_FORMAT;

public class GameNewsWidgetService extends RemoteViewsService {

  @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new NewsListRemoteViewsFactory(
        Injection.provideApplicationContext(),
        new GetFavouriteGamesNewsUseCase(
            Injection.provideAppScheduler(),
            Injection.provideDataRepository()
        ));
  }

  public static class NewsListRemoteViewsFactory
      implements RemoteViewsFactory {

    private Context context;
    private GetFavouriteGamesNewsUseCase getFavouriteGamesNewsUseCase;
    private List<Pulse> pulses;

    NewsListRemoteViewsFactory(Context applicationContext,
        GetFavouriteGamesNewsUseCase getFavouriteGamesNewsUseCase) {
      this.context = applicationContext;
      this.getFavouriteGamesNewsUseCase = getFavouriteGamesNewsUseCase;
    }

    @Override public void onCreate() {
    }

    @Override public void onDataSetChanged() {
      Timber.d("onDataSetChanged()");
      getFavouriteGamesNewsUseCase.setFromDatabase(true);
      getFavouriteGamesNewsUseCase.setListener(result -> {
        if (!result.loading() && result.succeeded()) {
          pulses = result.data;
          Timber.d(pulses.toString());
        }
      }).executeNow();
    }

    @Override public void onDestroy() {
    }

    @Override public int getCount() {
      return pulses != null ? pulses.size() : 0;
    }

    @Override public RemoteViews getViewAt(int position) {
      if (pulses != null) {
        Pulse pulse = pulses.get(position);
        if (pulse != null) {
          RemoteViews remoteViews =
              new RemoteViews(context.getPackageName(), R.layout.list_item_game_news_app_widget);
          remoteViews.setTextViewText(R.id.tv_news_title, pulse.getTitle());
          remoteViews.setTextViewText(R.id.tv_news_publish_date,
              DateUtil.getPrettyDate(pulse.getPublishedAt()));

          final Intent fillInIntent = new Intent();
          final Bundle extras = new Bundle();
          extras.putString(GameNewsAppWidget.EXTRA_NEWS_URL, pulse.getUrl());
          fillInIntent.putExtras(extras);
          remoteViews.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

          Image image = pulse.getPulseImage();
          if (image != null) {
            int size = context.getResources().getDimensionPixelSize(R.dimen.app_widget_thumb_size);

            String imageUrl = String.format(IMAGE_URL_FORMAT, IMAGE_THUMB, image.getCloudinaryId());
            try {
              RequestOptions requestOptions = new RequestOptions()
                  .placeholder(R.drawable.generic_placeholder)
                  .error(R.drawable.generic_placeholder)
                  .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
                  .autoClone();

              Bitmap bitmap = Glide.with(context)
                  .asBitmap()
                  .load(imageUrl)
                  .apply(requestOptions)
                  .submit(size, size)
                  .get();

              remoteViews.setImageViewBitmap(R.id.iv_news_thumb, bitmap);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          return remoteViews;
        }
      }
      return null;
    }

    @Override public RemoteViews getLoadingView() {
      return null;
    }

    @Override public int getViewTypeCount() {
      return 1;
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public boolean hasStableIds() {
      return false;
    }
  }
}
