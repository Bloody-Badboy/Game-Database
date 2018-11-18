package me.bloodybadboy.gamedatabase.ui.details.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Video;
import me.bloodybadboy.gamedatabase.databinding.ListItemVideoLayoutBinding;
import me.bloodybadboy.gamedatabase.utils.Utils;
import me.bloodybadboy.gamedatabase.utils.binding.BindableAdapter;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> implements
    BindableAdapter<List<Video>> {

  private final List<Video> videos = new ArrayList<>();
  private Context context;

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new ViewHolder(
        DataBindingUtil.inflate(inflater, R.layout.list_item_video_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    bindViewHolder(holder, videos.get(position));
  }

  @Override
  public int getItemCount() {
    return videos.size();
  }

  private void bindViewHolder(RecyclerView.ViewHolder viewHolder, Video video) {
    ViewHolder videoListItemViewHolder = (ViewHolder) viewHolder;
    if (video != null) {
      videoListItemViewHolder.binding.setVideo(video);
      videoListItemViewHolder.binding.getRoot().setTag(video);
    }
  }

  @Override public void setData(List<Video> data) {
    videos.addAll(data);
    notifyDataSetChanged();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ListItemVideoLayoutBinding binding;

    ViewHolder(ListItemVideoLayoutBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      binding.getRoot().setOnClickListener(v -> {
        Video video = (Video) v.getTag();
        launchYoutubeVideo(binding.getRoot().getContext(), video.getVideoId());
      });
    }

    void launchYoutubeVideo(Context context, String videoId) {
      if (videoId == null) {
        Toast.makeText(context, "Can't play the video.", Toast.LENGTH_SHORT).show();
      } else {
        Intent youtubeAppIntent =
            new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        if (Utils.canPerformIntent(context, youtubeAppIntent)) {
          context.startActivity(youtubeAppIntent);
        } else {
          Intent browserIntent =
              new Intent(Intent.ACTION_VIEW,
                  Uri.parse("http://www.youtube.com/watch?v=" + videoId));
          context.startActivity(browserIntent);
        }
      }
    }
  }
}
