package me.bloodybadboy.gamedatabase.ui.details.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.Constants;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Image;
import me.bloodybadboy.gamedatabase.databinding.ListItemImageLayoutBinding;
import me.bloodybadboy.gamedatabase.ui.PhotoViewActivity;
import me.bloodybadboy.gamedatabase.utils.binding.BindableAdapter;

import static me.bloodybadboy.gamedatabase.ui.PhotoViewActivity.EXTRA_IMAGE_URL;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> implements
    BindableAdapter<List<Image>> {

  private final List<Image> images = new ArrayList<>();
  private Activity activity;

  public ImagesAdapter(Activity activity) {
    this.activity = activity;
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new ViewHolder(
        DataBindingUtil.inflate(inflater, R.layout.list_item_image_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    bindViewHolder(holder, images.get(position));
  }

  @Override
  public int getItemCount() {
    return images.size();
  }

  private void bindViewHolder(RecyclerView.ViewHolder viewHolder, Image image) {
    ViewHolder videoListItemViewHolder = (ViewHolder) viewHolder;
    if (image != null) {
      videoListItemViewHolder.binding.setImage(image);
      videoListItemViewHolder.binding.getRoot().setTag(image);
    }
  }

  @Override public void setData(List<Image> data) {
    images.addAll(data);
    notifyDataSetChanged();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ListItemImageLayoutBinding binding;

    ViewHolder(ListItemImageLayoutBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      binding.getRoot().setOnClickListener(v -> {
        Image image = (Image) binding.getRoot().getTag();
        launchPhotoViewActivity(image);
      });
    }

    void launchPhotoViewActivity(Image image) {

      String imageUrl =
          String.format(Constants.IMAGE_URL_FORMAT, Constants.SCREENSHOT_SIZE_MID,
              image.getCloudinaryId());

      Intent intent = new Intent(activity, PhotoViewActivity.class);
      intent.putExtra(EXTRA_IMAGE_URL, imageUrl);

      //noinspection unchecked
      Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
          activity, binding.ivScreenshot, binding.ivScreenshot.getTransitionName()).toBundle();
      ActivityCompat.startActivity(activity, intent, options);
    }
  }
}
