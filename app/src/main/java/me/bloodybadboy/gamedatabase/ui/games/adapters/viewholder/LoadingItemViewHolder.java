package me.bloodybadboy.gamedatabase.ui.games.adapters.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import me.bloodybadboy.gamedatabase.databinding.ListItemLoadingLayoutBinding;

public class LoadingItemViewHolder extends RecyclerView.ViewHolder {
  public ListItemLoadingLayoutBinding binding;

  public LoadingItemViewHolder(ListItemLoadingLayoutBinding binding) {
    super(binding.getRoot());
    this.binding = binding;
  }
}