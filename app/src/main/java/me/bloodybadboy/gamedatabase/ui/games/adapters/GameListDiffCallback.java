package me.bloodybadboy.gamedatabase.ui.games.adapters;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Game;

public class GameListDiffCallback extends DiffUtil.Callback {
  private final List<Game> oldGameList;
  private final List<Game> newGameList;

  GameListDiffCallback(List<Game> oldGameList, List<Game> newGameList) {
    this.oldGameList = oldGameList;
    this.newGameList = newGameList;
  }

  @Override public int getOldListSize() {
    return oldGameList == null ? 0 : oldGameList.size();
  }

  @Override public int getNewListSize() {
    return newGameList == null ? 0 : newGameList.size();
  }

  @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    if (oldGameList.get(oldItemPosition) == null || newGameList.get(newItemPosition) == null) {
      return true;
    }
    return oldGameList.get(oldItemPosition).getId() == newGameList.get(newItemPosition)
        .getId();
  }

  @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    final Game oldGame = oldGameList.get(oldItemPosition);
    final Game newGame = newGameList.get(newItemPosition);
    if (oldGame == null || newGame == null) {
      return true;
    }
    return oldGame.equals(newGame);
  }
}
