package me.bloodybadboy.gamedatabase.ui.games;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

  private static final int VISIBLE_THRESHOLD = 5;
  private boolean isLoading = false;
  private LinearLayoutManager layoutManager;

  public OnLoadMoreScrollListener(LinearLayoutManager layoutManager) {
    this.layoutManager = layoutManager;
  }

  public boolean isLoading() {
    return isLoading;
  }

  public void notifyDataLoaded() {
    this.isLoading = false;
  }

  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);
  }

  @Override
  public void onScrolled(RecyclerView view, int dx, int dy) {
    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
    int totalItemCount = layoutManager.getItemCount();

    if (!isLoading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount) {
      isLoading = true;
      onLoadMore();
    }
  }

  public abstract void onLoadMore();
}