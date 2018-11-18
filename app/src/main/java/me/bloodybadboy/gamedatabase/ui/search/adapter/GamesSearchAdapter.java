package me.bloodybadboy.gamedatabase.ui.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.databinding.ListItemSearchGameLayoutBinding;
import me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity;

import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_CLOUDINARY_ID;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_ID;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_NAME;

@SuppressWarnings("WeakerAccess") public class GamesSearchAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Activity activity;
  private List<Game> gameList = new ArrayList<>();

  public GamesSearchAdapter(Activity activity) {
    this.activity = activity;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    ListItemSearchGameLayoutBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.list_item_search_game_layout, parent,
            false);
    return new GameSearchListItemViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    bindGameListItemViewHolder(viewHolder, gameList.get(position));
  }

  @Override public int getItemCount() {
    return gameList != null ? gameList.size() : 0;
  }

  private void bindGameListItemViewHolder(RecyclerView.ViewHolder viewHolder, Game game) {
    GameSearchListItemViewHolder movieListItemViewHolder =
        (GameSearchListItemViewHolder) viewHolder;
    if (game != null) {
      movieListItemViewHolder.binding.setGame(game);
      movieListItemViewHolder.binding.getRoot().setTag(game);
    }
  }

  public void setData(List<Game> data) {
    gameList.clear();
    gameList.addAll(data);
    notifyDataSetChanged();
  }

  class GameSearchListItemViewHolder extends RecyclerView.ViewHolder {

    ListItemSearchGameLayoutBinding binding;

    GameSearchListItemViewHolder(ListItemSearchGameLayoutBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      binding.getRoot().setOnClickListener(v -> {
        Game game = (Game) v.getTag();
        launchDetailsActivity(game);
      });
    }

    void launchDetailsActivity(Game game) {

      Intent intent = new Intent(activity, GameDetailsActivity.class);
      intent.putExtra(EXTRA_GAME_ID, game.getId());
      intent.putExtra(EXTRA_GAME_NAME, game.getName());
      intent.putExtra(EXTRA_GAME_CLOUDINARY_ID, game.getCover().getCloudinaryId());

      //noinspection unchecked
      Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
          activity,
          new Pair<>(binding.ivGameCover, binding.ivGameCover.getTransitionName()),
          new Pair<>(binding.tvGameName, binding.tvGameName.getTransitionName())
      ).toBundle();
      ActivityCompat.startActivity(activity, intent, options);
    }
  }
}
