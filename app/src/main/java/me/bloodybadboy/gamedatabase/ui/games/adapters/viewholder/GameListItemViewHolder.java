package me.bloodybadboy.gamedatabase.ui.games.adapters.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.databinding.ListItemGameLayoutBinding;
import me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity;

import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_ADAPTER_POSITION;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_CLOUDINARY_ID;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_ID;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_NAME;
import static me.bloodybadboy.gamedatabase.ui.games.GameListActivity.REQUEST_CODE;

public class GameListItemViewHolder extends RecyclerView.ViewHolder {

  public ListItemGameLayoutBinding binding;
  private Activity activity;

  public GameListItemViewHolder(ListItemGameLayoutBinding binding, Activity activity) {
    super(binding.getRoot());
    this.binding = binding;
    this.activity = activity;
    binding.getRoot().setOnClickListener(v -> {
      Game game = (Game) v.getTag();
      launchDetailsActivity(game);
    });
  }

  private void launchDetailsActivity(Game game) {

    Intent intent = new Intent(activity, GameDetailsActivity.class);
    intent.putExtra(EXTRA_GAME_ID, game.getId());
    intent.putExtra(EXTRA_GAME_NAME, game.getName());
    intent.putExtra(EXTRA_GAME_CLOUDINARY_ID, game.getCover().getCloudinaryId());
    intent.putExtra(EXTRA_ADAPTER_POSITION, getAdapterPosition());

    //noinspection unchecked
    Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
        activity,
        new Pair<>(binding.ivGameCover, binding.ivGameCover.getTransitionName()),
        new Pair<>(binding.tvGameName, binding.tvGameName.getTransitionName())
    ).toBundle();
    ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE, options);
  }
}