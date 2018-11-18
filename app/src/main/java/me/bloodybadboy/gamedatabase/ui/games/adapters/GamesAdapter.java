package me.bloodybadboy.gamedatabase.ui.games.adapters;

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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.databinding.ListItemGameLayoutBinding;
import me.bloodybadboy.gamedatabase.databinding.ListItemLoadingLayoutBinding;
import me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity;
import timber.log.Timber;

import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_ADAPTER_POSITION;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_CLOUDINARY_ID;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_ID;
import static me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity.EXTRA_GAME_NAME;
import static me.bloodybadboy.gamedatabase.ui.games.GameListActivity.REQUEST_CODE;

@SuppressWarnings("WeakerAccess") public class GamesAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int VIEW_TYPE_ITEM = 0;
  public static final int VIEW_TYPE_LOADING = 1;

  private Activity activity;
  private List<Game> gameList = new ArrayList<>();
  private boolean isLoadingItemAdded = false;

  public GamesAdapter(Activity activity) {
    this.activity = activity;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    if (viewType == VIEW_TYPE_ITEM) {
      return new GameListItemViewHolder(
          DataBindingUtil.inflate(layoutInflater, R.layout.list_item_game_layout, parent, false));
    } else {
      return new LoadingItemViewHolder(
          DataBindingUtil.inflate(layoutInflater, R.layout.list_item_loading_layout, parent,
              false));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (getItemViewType(position) == VIEW_TYPE_ITEM) {
      bindGameListItemViewHolder(viewHolder, gameList.get(position));
    }
  }

  @Override public int getItemCount() {
    return gameList != null ? gameList.size() : 0;
  }

  @Override public int getItemViewType(int position) {
    return gameList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }

  public void swapGameListItems(List<Game> games) {
    Timber.d("swapGameListItems()");
    if (games == null) {
      Timber.e(" swapGameListItems -> movies == null");
      return;
    }
    gameList.clear();
    gameList.addAll(games);
    notifyDataSetChanged();
  }

  public void updateGameListItems(List<Game> games) {
    Timber.d("updateGameListItems()");
    if (games == null) {
      Timber.e("updateGameListItems -> games == null");
      return;
    }
    final GameListDiffCallback movieListDiffCallback = new GameListDiffCallback(gameList, games);
    final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(movieListDiffCallback);

    gameList.clear();
    gameList.addAll(games);

    diffResult.dispatchUpdatesTo(GamesAdapter.this);
  }

  public void addLoadingItem() {
    if (!isLoadingItemAdded) {
      isLoadingItemAdded = true;
      gameList.add(null);
      notifyItemInserted(gameList.size() - 1);
    }
  }

  public void removeLoadingItem() {
    if (isLoadingItemAdded) {
      isLoadingItemAdded = false;
      gameList.remove(gameList.size() - 1);
      notifyItemRemoved(gameList.size());
    }
  }

  private void bindGameListItemViewHolder(RecyclerView.ViewHolder viewHolder, Game game) {
    GameListItemViewHolder movieListItemViewHolder = (GameListItemViewHolder) viewHolder;
    if (game != null) {
      movieListItemViewHolder.binding.setGame(game);
      movieListItemViewHolder.binding.getRoot().setTag(game);
    }
  }

  class GameListItemViewHolder extends RecyclerView.ViewHolder {

    ListItemGameLayoutBinding binding;

    GameListItemViewHolder(ListItemGameLayoutBinding binding) {
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

  class LoadingItemViewHolder extends RecyclerView.ViewHolder {
    ListItemLoadingLayoutBinding binding;

    LoadingItemViewHolder(ListItemLoadingLayoutBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
