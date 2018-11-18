package me.bloodybadboy.gamedatabase.utils.binding;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Esrb;
import timber.log.Timber;

public final class BindingAdapters {
  @BindingAdapter(value = { "imageUrl" })
  public static void loadDefaultImage(ImageView imageView, String imageUrl) {
    loadImageFromUrl(imageView, imageUrl, false);
  }

  @BindingAdapter(value = { "circleImageUrl" })
  public static void loadCircleImage(ImageView imageView, String imageUrl) {
    loadImageFromUrl(imageView, imageUrl, true);
  }

  private static void loadImageFromUrl(ImageView imageView, String imageUrl, Boolean circleCrop) {

    if (imageUrl == null) {
      Timber.d("imageUrl == null");
      Glide.with(imageView)
          .load(R.drawable.generic_placeholder)
          .into(imageView);
    } else {

      RequestOptions requestOptions = new RequestOptions()
          .placeholder(R.drawable.generic_placeholder)
          .diskCacheStrategy(DiskCacheStrategy.ALL);
      if (circleCrop) {
        requestOptions.circleCrop()
            .autoClone();
      }
      Glide.with(imageView)
          .load(imageUrl)
          .transition(new DrawableTransitionOptions().crossFade(500))
          .apply(requestOptions)
          .into(imageView);
    }
  }

  @BindingAdapter(value = { "imageFromEsrb" })
  public static void imageFromEsrb(ImageView imageView, Esrb esrb) {
    if (esrb == null) {
      imageView.setVisibility(View.INVISIBLE);
      return;
    }
    String esrbRating = esrb.getRating();
    if (esrbRating == null) {
      imageView.setVisibility(View.INVISIBLE);
      return;
    }
    int rating = Integer.parseInt(esrb.getRating());
    if (rating == 1) {
      imageView.setImageResource(R.drawable.rating_rp);
    } else if (rating == 2) {
      imageView.setImageResource(R.drawable.rating_ec);
    } else if (rating == 3) {
      imageView.setImageResource(R.drawable.rating_e);
    } else if (rating == 4) {
      imageView.setImageResource(R.drawable.rating_e10);
    } else if (rating == 5) {
      imageView.setImageResource(R.drawable.rating_t);
    } else if (rating == 6) {
      imageView.setImageResource(R.drawable.rating_m);
    } else if (rating == 7) {
      imageView.setImageResource(R.drawable.rating_ao);
    } else {
      imageView.setVisibility(View.INVISIBLE);
    }
  }

  @BindingAdapter(value = { "textFromEsrb" })
  public static void textFromEsrb(TextView textView, Esrb esrb) {
    if (esrb == null) {
      textView.setVisibility(View.INVISIBLE);
      return;
    }
    String esrbRating = esrb.getRating();
    if (esrbRating == null) {
      textView.setVisibility(View.INVISIBLE);
      return;
    }
    int rating = Integer.parseInt(esrb.getRating());
    if (rating == 1) {
      textView.setText(textView.getContext().getString(R.string.esrb_rating_pending));
    } else if (rating == 2) {
      textView.setText(textView.getContext().getString(R.string.esrb_early_childhood));
    } else if (rating == 3) {
      textView.setText(textView.getContext().getString(R.string.esrb_everyone));
    } else if (rating == 4) {
      textView.setText(textView.getContext().getString(R.string.esrb_everyone_10));
    } else if (rating == 5) {
      textView.setText(textView.getContext().getString(R.string.esrb_teen));
    } else if (rating == 6) {
      textView.setText(textView.getContext().getString(R.string.esrb_mature_17));
    } else if (rating == 7) {
      textView.setText(textView.getContext().getString(R.string.esrb_adults_only_18));
    } else {
      textView.setVisibility(View.INVISIBLE);
    }
  }

  @BindingAdapter(value = { "data", "noDataView" })
  public static <T> void setRecyclerViewData(RecyclerView recyclerView, T data,
      View notFoundView) {
    if (data == null || data instanceof List && ((List) data).size() <= 0) {
      if (notFoundView != null) {
        recyclerView.setVisibility(View.GONE);
        notFoundView.setVisibility(View.VISIBLE);
      }
      return;
    }

    recyclerView.setVisibility(View.VISIBLE);
    notFoundView.setVisibility(View.GONE);

    if (recyclerView.getAdapter() instanceof BindableAdapter<?>) {
      //noinspection unchecked
      ((BindableAdapter<T>) recyclerView.getAdapter()).setData(data);
    }
  }
}
