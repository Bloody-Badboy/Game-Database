package me.bloodybadboy.gamedatabase.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.databinding.ActivityPhotoViewBinding;

public class PhotoViewActivity extends AppCompatActivity {

  public static final String EXTRA_IMAGE_URL = "image_url";

  ActivityPhotoViewBinding binding;
  private String imageUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_view);

    setSupportActionBar(binding.toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    Intent intent = getIntent();
    if (intent.hasExtra(EXTRA_IMAGE_URL)) {
      imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
    } else {
      Toast.makeText(this, R.string.photo_view_msg_missing_extra, Toast.LENGTH_LONG).show();
      finish();
    }

    supportPostponeEnterTransition();

    RequestOptions requestOptions = new RequestOptions()
        .placeholder(R.drawable.generic_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    Glide.with(this).load(imageUrl)
        .apply(requestOptions)
        .listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e, Object model,
              Target<Drawable> target,
              boolean isFirstResource) {
            return false;
          }

          @Override
          public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
              DataSource dataSource, boolean isFirstResource) {
            supportStartPostponedEnterTransition();
            return false;
          }
        }).into(binding.photoView);
  }

  @Override public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
