package me.bloodybadboy.gamedatabase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import me.bloodybadboy.gamedatabase.ui.games.GameListActivity;

public class LauncherActivity extends AppCompatActivity {

  public static final long GAME_LIST_SCREEN_DELAY = 1500L;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new Handler().postDelayed(() -> {
      startActivity(new Intent(LauncherActivity.this, GameListActivity.class));
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      finish();
    }, GAME_LIST_SCREEN_DELAY);
  }
}
