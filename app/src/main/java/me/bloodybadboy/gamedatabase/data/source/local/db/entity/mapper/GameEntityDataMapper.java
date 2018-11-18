package me.bloodybadboy.gamedatabase.data.source.local.db.entity.mapper;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Esrb;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Image;
import me.bloodybadboy.gamedatabase.data.model.Theme;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.GameEntity;
import me.bloodybadboy.gamedatabase.injection.Injection;
import timber.log.Timber;

public class GameEntityDataMapper {

  private final JsonAdapter<List<Theme>> themesJsonAdapter;

  public GameEntityDataMapper() {
    Moshi moshi = Injection.provideMoshi();
    themesJsonAdapter = moshi.adapter(Types.newParameterizedType(List.class, Theme.class));
  }

  public Game transform(GameEntity gameEntity) {
    Game game = null;
    if (gameEntity != null) {
      game = new Game();

      Image coverImage = new Image();
      coverImage.setCloudinaryId(gameEntity.getCoverCloudinaryId());

      Esrb esrb = new Esrb();
      esrb.setRating(gameEntity.getEsrbRating());

      List<Theme> themes = new ArrayList<>();
      if (gameEntity.getThemesJson() != null) {
        try {
          List<Theme> themeList = themesJsonAdapter.fromJson(gameEntity.getThemesJson());
          if (themeList != null) {
            themes.addAll(themeList);
          }
        } catch (IOException e) {
          Timber.e(e);
        }
      }

      game.setId(gameEntity.getId());
      game.setName(gameEntity.getName());
      game.setCover(coverImage);
      game.setFirstReleaseDate(gameEntity.getFirstReleaseDate());
      game.setRating(gameEntity.getRating());
      game.setEsrb(esrb);
      game.setThemes(themes);
    }
    return game;
  }

  public GameEntity transform(Game game) {
    GameEntity gameEntity = null;
    if (game != null) {
      gameEntity = new GameEntity();

      Image cover = game.getCover();
      String coverCloudinaryId = null;
      if (cover != null) {
        coverCloudinaryId = cover.getCloudinaryId();
      }

      Esrb esrb = game.getEsrb();

      String esrbRating = null;
      if (esrb != null) {
        esrbRating = esrb.getRating();
      }

      List<Theme> themes = game.getThemes();
      String themesJson = null;
      if (themes != null) {
        themesJson = themesJsonAdapter.toJson(themes);
      }

      gameEntity.setId(game.getId());
      gameEntity.setName(game.getName());
      gameEntity.setCoverCloudinaryId(coverCloudinaryId);
      gameEntity.setFirstReleaseDate(game.getFirstReleaseDate());
      gameEntity.setRating(game.getRating());
      gameEntity.setEsrbRating(esrbRating);
      gameEntity.setThemesJson(themesJson);
    }

    return gameEntity;
  }

  public List<Game> transformToGame(Collection<GameEntity> gameEntityCollection) {
    final List<Game> gameList = new ArrayList<>();
    for (GameEntity gameEntity : gameEntityCollection) {
      final Game game = transform(gameEntity);
      if (game != null) {
        gameList.add(game);
      }
    }
    return gameList;
  }

  public List<GameEntity> transformToEntity(Collection<Game> gameCollection) {
    final List<GameEntity> gameEntityList = new ArrayList<>();
    for (Game game : gameCollection) {
      final GameEntity gameEntity = transform(game);
      if (gameEntity != null) {
        gameEntityList.add(gameEntity);
      }
    }
    return gameEntityList;
  }
}
