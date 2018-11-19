package me.bloodybadboy.gamedatabase.ui.games.adapters;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Game;

public class GameListDiffCallback extends DiffUtil.Callback {
  private final List<Object> oldGameList;
  private final List<Object> newGameList;

  GameListDiffCallback(List<Object> oldGameList, List<Object> newGameList) {
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

    final Object oldGame = oldGameList.get(oldItemPosition);
    final Object newGame = newGameList.get(newItemPosition);

    if (oldGame == null || newGame == null) {
      return true;
    }
    if (oldGame instanceof Game && newGame instanceof Game) {
      return ((Game) oldGame).getId() == ((Game) newGame).getId();
    }
    return oldGame.equals(newGame);
  }

  @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    final Object oldGame = oldGameList.get(oldItemPosition);
    final Object newGame = newGameList.get(newItemPosition);
    if (oldGame == null || newGame == null) {
      return true;
    }
    return oldGame.equals(newGame);
  }
}
