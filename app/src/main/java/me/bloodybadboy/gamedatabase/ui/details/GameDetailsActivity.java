package me.bloodybadboy.gamedatabase.ui.details;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.google.android.material.snackbar.Snackbar;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Game;
import me.bloodybadboy.gamedatabase.data.model.Image;
import me.bloodybadboy.gamedatabase.databinding.ActivityGameDetailsBinding;
import me.bloodybadboy.gamedatabase.ui.details.adapters.ImagesAdapter;
import me.bloodybadboy.gamedatabase.ui.details.adapters.NewsAdapter;
import me.bloodybadboy.gamedatabase.ui.details.adapters.VideosAdapter;
import me.bloodybadboy.gamedatabase.utils.NetworkUtil;
import me.bloodybadboy.gamedatabase.utils.event.EventObserver;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class GameDetailsActivity extends DaggerAppCompatActivity {

  public static final String EXTRA_GAME_ID = "game_id";
  public static final String EXTRA_GAME_NAME = "game_name";
  public static final String EXTRA_GAME_CLOUDINARY_ID = "game_cover_cloudinary_id";
  public static final String EXTRA_ADAPTER_POSITION = "adapter_position";
  public static final String EXTRA_ITEM_REMOVED = "removed_from_favourites";

  private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR =
      new AccelerateInterpolator();
  private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
  @Inject
  ViewModelProvider.Factory viewModelFactory;
  private ActivityGameDetailsBinding binding;
  private GameDetailsViewModel viewModel;
  private Game game;
  private long gameId;
  private String gameName;
  private String gameCoverCloudinaryId;
  private int adapterPosition = NO_POSITION;
  private boolean isGameInFavourite;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_game_details);
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameDetailsViewModel.class);

    binding.setViewModel(viewModel);
    binding.setLifecycleOwner(this);
    binding.content.setLifecycleOwner(this);

    setSupportActionBar(binding.toolbar);

    Intent intent = getIntent();

    adapterPosition = intent.getIntExtra(EXTRA_ADAPTER_POSITION, NO_POSITION);
    if (intent.hasExtra(EXTRA_GAME_ID) &&
        intent.hasExtra(EXTRA_GAME_NAME) &&
        intent.hasExtra(EXTRA_GAME_CLOUDINARY_ID)) {
      gameId = intent.getLongExtra(EXTRA_GAME_ID, -1);
      gameName = intent.getStringExtra(EXTRA_GAME_NAME);
      gameCoverCloudinaryId = intent.getStringExtra(EXTRA_GAME_CLOUDINARY_ID);
    } else {
      Toast.makeText(this, R.string.game_detail_msg_missing_extra, Toast.LENGTH_LONG).show();
      finish();
    }

    if (gameId < 0) {
      Toast.makeText(this, R.string.game_detail_msg_invalid_game_id, Toast.LENGTH_LONG).show();
    }

    // Tag numbers reference: https://igdb.github.io/api/references/tag-numbers/
    int gameTypeID = 3;
    int gameTagNumber = (gameTypeID << 28) | (int) gameId;

    binding.appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
      if (verticalOffset <= -appBarLayout.getTotalScrollRange() + binding.toolbar.getHeight()) {
        binding.toolbar.setTitle(gameName);
      } else {
        binding.toolbar.setTitle(null);
      }
    });

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    Image coverImage = new Image();
    coverImage.setCloudinaryId(gameCoverCloudinaryId);

    binding.setCoverImage(coverImage);
    binding.content.tvGameName.setText(gameName);

    initRecycleViews();

    viewModel.game.observe(this, game -> {
      this.game = game;
      binding.setGame(game);
    });

    viewModel.bannerImage.observe(this, image -> binding.setBannerImage(image));

    viewModel.exceptionEvent.observe(this, exceptionEvent -> viewModel.showRetrySnackBar(
        NetworkUtil.isOnline() ? getString(R.string.error_msg_unexpected)
            : getString(R.string.error_msg_no_internet)));

    viewModel.pulses.observe(this, pulses -> {
      NewsAdapter adapter = (NewsAdapter) binding.content.rvNews.getAdapter();
      if (adapter != null) {
        adapter.setPulseList(pulses);
      }
    });

    viewModel.isFavourite.observe(this, isFavourite -> {
      isGameInFavourite = isFavourite;
      if (isFavourite) {
        playFabAnimation(true);
      } else {
        playFabAnimation(false);
      }
    });

    viewModel.showRetrySnackBarEvent.observe(this, new EventObserver<>(this::showErrorSnackbar));

    viewModel.shareGameEvent.observe(this, new EventObserver<>(o -> startShareGameIntent()));

    if (savedInstanceState == null) {
      viewModel.requestForGameDetails(gameId);
      viewModel.requestForGameNews(gameTagNumber);
      viewModel.checkIsGameInFavourite(gameId);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_game_details, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_share) {
      viewModel.onClickShareGame();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private void initRecycleViews() {
    RecyclerView.LayoutManager videoListLayoutManager =
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
    RecyclerView.LayoutManager imageListLayoutManager =
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
    RecyclerView.LayoutManager newsListLayoutManager =
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

    SnapHelper videoListSnapHelper = new GravityPagerSnapHelper(Gravity.START);
    SnapHelper imageListSnapHelper = new GravityPagerSnapHelper(Gravity.START);
    SnapHelper newsListSnapHelper = new GravityPagerSnapHelper(Gravity.START);

    binding.content.rvVideos.setLayoutManager(videoListLayoutManager);
    binding.content.rvVideos.setHasFixedSize(true);
    binding.content.rvVideos.setAdapter(new VideosAdapter());
    videoListSnapHelper.attachToRecyclerView(binding.content.rvVideos);

    binding.content.rvImages.setLayoutManager(imageListLayoutManager);
    binding.content.rvImages.setHasFixedSize(true);
    binding.content.rvImages.setAdapter(new ImagesAdapter(this));
    imageListSnapHelper.attachToRecyclerView(binding.content.rvImages);

    binding.content.rvNews.setLayoutManager(newsListLayoutManager);
    binding.content.rvNews.setHasFixedSize(true);
    binding.content.rvNews.setAdapter(new NewsAdapter());
    newsListSnapHelper.attachToRecyclerView(binding.content.rvNews);
  }

  void playFabAnimation(final boolean isAddedToFavourites) {

    AnimatorSet animatorSet = new AnimatorSet();

    ObjectAnimator rotationAnim =
        ObjectAnimator.ofFloat(binding.content.fabAddToFavourites, "rotation", 0f, 360f);
    rotationAnim.setDuration(300);
    rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

    ObjectAnimator bounceAnimX =
        ObjectAnimator.ofFloat(binding.content.fabAddToFavourites, "scaleX", 0.2f, 1f);
    bounceAnimX.setDuration(300);
    bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

    ObjectAnimator bounceAnimY =
        ObjectAnimator.ofFloat(binding.content.fabAddToFavourites, "scaleY", 0.2f, 1f);
    bounceAnimY.setDuration(300);
    bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);

    bounceAnimY.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        if (isAddedToFavourites) {
          binding.content.fabAddToFavourites.setImageResource(R.drawable.ic_heart_active);
        } else {
          binding.content.fabAddToFavourites.setImageResource(R.drawable.ic_heart_inactive);
        }
      }
    });
    animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
    animatorSet.start();
  }

  @Override public void onBackPressed() {
    Intent data = new Intent();
    data.putExtra(EXTRA_ADAPTER_POSITION, adapterPosition);
    data.putExtra(EXTRA_ITEM_REMOVED, !isGameInFavourite);
    setResult(RESULT_OK, data);
    super.onBackPressed();
  }

  private void showErrorSnackbar(String message) {
    Snackbar snackbar =
        Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction(R.string.all_retry, v -> {
      if (NetworkUtil.isOnline()) {
        viewModel.onClickRetry();
      } else {
        showErrorSnackbar(message);
      }
    });

    snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
    View sbView = snackbar.getView();
    TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    snackbar.show();
  }

  private void startShareGameIntent() {
    if (game == null) {
      return;
    }
    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setChooserTitle(getString(R.string.game_detail_title_share_via))
        .setText(
            String.format(getString(R.string.game_detail_share_game_content), game.getName(),
                game.getUrl()))
        .getIntent(), getString(R.string.action_share)));
  }
}
