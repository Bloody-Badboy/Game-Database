package me.bloodybadboy.gamedatabase.ui.games.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.ui.games.adapters.viewholder.GameListItemViewHolder;
import me.bloodybadboy.gamedatabase.ui.games.adapters.viewholder.LoadingItemViewHolder;
import me.bloodybadboy.gamedatabase.ui.games.adapters.viewholder.UnifiedNativeAdItemViewHolder;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess") public class GamesAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int VIEW_TYPE_ITEM = 0;
  public static final int VIEW_TYPE_LOADING = 1;
  public static final int VIEW_TYPE_UNIFIED_NATIVE_AD = 2;

  private Activity activity;
  private List<Object> items = new ArrayList<>();
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
          DataBindingUtil.inflate(layoutInflater, R.layout.list_item_game_layout, parent, false),
          activity);
    } else if (viewType == VIEW_TYPE_LOADING) {
      return new LoadingItemViewHolder(
          DataBindingUtil.inflate(layoutInflater, R.layout.list_item_loading_layout, parent,
              false));
    } else if (viewType == VIEW_TYPE_UNIFIED_NATIVE_AD) {
      return new UnifiedNativeAdItemViewHolder(
          DataBindingUtil.inflate(layoutInflater, R.layout.list_item_unified_native_ad, parent,
              false));
    } else {
      throw new IllegalArgumentException("Invalid viewType: " + viewType);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (getItemViewType(position) == VIEW_TYPE_ITEM) {
      bindGameListItemViewHolder(viewHolder, (Game) items.get(position));
    } else if (getItemViewType(position) == VIEW_TYPE_UNIFIED_NATIVE_AD) {
      bindNativeAdItemViewHolder(viewHolder, (UnifiedNativeAd) items.get(position));
    }
  }

  @Override public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  @Override public int getItemViewType(int position) {
    if (items.get(position) == null) {
      return VIEW_TYPE_LOADING;
    } else {
      if (items.get(position) instanceof UnifiedNativeAd) {
        return VIEW_TYPE_UNIFIED_NATIVE_AD;
      } else {
        return VIEW_TYPE_ITEM;
      }
    }
  }

  public void swapListItems(List<Object> games) {
    Timber.d("swapListItems()");
    if (games == null) {
      Timber.e(" swapListItems -> games == null");
      return;
    }
    items.clear();
    items.addAll(games);
    notifyDataSetChanged();
  }

  public void updateListItems(List<Object> games) {
    Timber.d("updateListItems()");
    if (games == null) {
      Timber.e("updateListItems -> games == null");
      return;
    }
    final GameListDiffCallback movieListDiffCallback = new GameListDiffCallback(items, games);
    final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(movieListDiffCallback);

    items.clear();
    items.addAll(games);

    diffResult.dispatchUpdatesTo(GamesAdapter.this);
  }

  public void addLoadingItem() {
    if (!isLoadingItemAdded) {
      isLoadingItemAdded = true;
      items.add(null);
      notifyItemInserted(items.size() - 1);
    }
  }

  public void removeLoadingItem() {
    if (isLoadingItemAdded) {
      isLoadingItemAdded = false;
      items.remove(items.size() - 1);
      notifyItemRemoved(items.size());
    }
  }

  private void bindGameListItemViewHolder(RecyclerView.ViewHolder viewHolder, Game game) {
    GameListItemViewHolder gameListItemViewHolder = (GameListItemViewHolder) viewHolder;
    if (game != null) {
      gameListItemViewHolder.binding.setGame(game);
      gameListItemViewHolder.binding.getRoot().setTag(game);
    }
  }

  private void bindNativeAdItemViewHolder(RecyclerView.ViewHolder viewHolder,
      UnifiedNativeAd nativeAd) {
    UnifiedNativeAdItemViewHolder unifiedNativeAdItemViewHolder =
        (UnifiedNativeAdItemViewHolder) viewHolder;
    if (nativeAd != null) {
      unifiedNativeAdItemViewHolder.bind(nativeAd
      );
      unifiedNativeAdItemViewHolder.binding.getRoot().setTag(nativeAd);
    }
  }
}
