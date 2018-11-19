package me.bloodybadboy.gamedatabase.ui.games.adapters.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import me.bloodybadboy.gamedatabase.databinding.ListItemUnifiedNativeAdBinding;

public class UnifiedNativeAdItemViewHolder extends RecyclerView.ViewHolder {
  public ListItemUnifiedNativeAdBinding binding;

 public UnifiedNativeAdItemViewHolder(ListItemUnifiedNativeAdBinding binding) {
    super(binding.getRoot());
    this.binding = binding;

    binding.adView.setHeadlineView(binding.adHeadline);
    binding.adView.setBodyView(binding.adBody);
    binding.adView.setCallToActionView(binding.adCallToAction);
    binding.adView.setIconView(binding.adIcon);
    binding.adView.setStarRatingView(binding.adStars);
    binding.adView.setAdvertiserView(binding.adAdvertiser);
  }

  public void bind(UnifiedNativeAd nativeAd) {
    ((TextView) binding.adView.getHeadlineView()).setText(nativeAd.getHeadline());
    ((TextView) binding.adView.getBodyView()).setText(nativeAd.getBody());
    ((Button) binding.adView.getCallToActionView()).setText(nativeAd.getCallToAction());

    NativeAd.Image icon = nativeAd.getIcon();

    if (icon == null) {
      binding.adView.getIconView().setVisibility(View.INVISIBLE);
    } else {
      ((ImageView) binding.adView.getIconView()).setImageDrawable(icon.getDrawable());
      binding.adView.getIconView().setVisibility(View.VISIBLE);
    }

    if (nativeAd.getStarRating() == null) {
      binding.adView.getStarRatingView().setVisibility(View.INVISIBLE);
    } else {
      ((RatingBar) binding.adView.getStarRatingView())
          .setRating(nativeAd.getStarRating().floatValue());
      binding.adView.getStarRatingView().setVisibility(View.VISIBLE);
    }

    if (nativeAd.getAdvertiser() == null) {
      binding.adView.getAdvertiserView().setVisibility(View.INVISIBLE);
    } else {
      ((TextView) binding.adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
      binding.adView.getAdvertiserView().setVisibility(View.VISIBLE);
    }

    // Assign native ad object to the native view.
    binding.adView.setNativeAd(nativeAd);
  }
}
