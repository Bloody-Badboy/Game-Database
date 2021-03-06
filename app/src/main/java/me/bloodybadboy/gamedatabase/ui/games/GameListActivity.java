package me.bloodybadboy.gamedatabase.ui.games;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.snackbar.Snackbar;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.Constants.GameListFilterType;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.databinding.ActivityGameListBinding;
import me.bloodybadboy.gamedatabase.sync.JobSchedulerService;
import me.bloodybadboy.gamedatabase.ui.details.GameDetailsActivity;
import me.bloodybadboy.gamedatabase.ui.games.adapters.GamesAdapter;
import me.bloodybadboy.gamedatabase.ui.search.GameSearchActivity;
import me.bloodybadboy.gamedatabase.utils.NetworkUtil;
import me.bloodybadboy.gamedatabase.utils.event.EventObserver;
import timber.log.Timber;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;
import static me.bloodybadboy.gamedatabase.Constants.GameListFilterType.FAVOURITES;

public class GameListActivity extends DaggerAppCompatActivity {

  public static final int REQUEST_CODE = 100;
  public static final int NUMBER_OF_ADS = 5;
  private static final int JOB_ID = 101;
  private static final long REFRESH_INTERVAL = 86400000; // 24 hrs
  @Inject
  ViewModelProvider.Factory viewModelFactory;
  private ActivityGameListBinding binding;
  private GameListViewModel viewModel;
  private GameListFilterType gameListFilterType;
  private GamesAdapter gamesAdapter;
  private OnLoadMoreScrollListener onLoadMoreScrollListener;
  private boolean shouldSwapList;
  private AdLoader adLoader;
  private List<UnifiedNativeAd> nativeAds = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_game_list);

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameListViewModel.class);

    binding.setViewModel(viewModel);
    binding.setLifecycleOwner(this);

    setSupportActionBar(binding.toolbar);

    gamesAdapter = new GamesAdapter(this);

    MobileAds.initialize(this, getString(R.string.admob_app_id));

    initRecycleView();
    scheduleJob();

    viewModel.objectListResult.observe(this, listResult -> {
      if (onLoadMoreScrollListener.isLoading()) {
        onLoadMoreScrollListener.notifyDataLoaded();
      }

      if (shouldSwapList) {
        shouldSwapList = false;
        binding.recyclerView.scheduleLayoutAnimation();
        gamesAdapter.swapListItems(listResult);
        binding.recyclerView.scrollToPosition(0);
      } else {
        gamesAdapter.updateListItems(listResult);
      }
    });

    viewModel.exceptionEvent.observe(this, new EventObserver<>(e -> {
      if (onLoadMoreScrollListener.isLoading()) {
        onLoadMoreScrollListener.notifyDataLoaded();
      }
      viewModel.showRetrySnackBar(
          NetworkUtil.isOnline() ? getString(R.string.error_msg_unexpected)
              : getString(R.string.error_msg_no_internet));
    }));

    viewModel.listFilterType.observe(this, filterType -> gameListFilterType = filterType);

    viewModel.swapGameList.observe(this, new EventObserver<>(swapList -> {
      Timber.d("swapList: %s", swapList);
      shouldSwapList = swapList;
    }));

    viewModel.gameListLoading.observe(this, isLoading -> {
      if (isLoading) {
        gamesAdapter.addLoadingItem();
      } else {
        gamesAdapter.removeLoadingItem();
      }
    });

    viewModel.showRetrySnackBarEvent.observe(this, new EventObserver<>(this::showErrorSnackbar));

    viewModel.loadMoreNativeAds.observe(this, new EventObserver<>(o -> {
      nativeAds.clear();
      loadNativeAds();
    }));

    if (savedInstanceState == null) {
      loadNativeAds();
    }
  }

  private void initRecycleView() {
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

    onLoadMoreScrollListener = new OnLoadMoreScrollListener(linearLayoutManager) {
      @Override public void onLoadMore() {
        Timber.d("onLoadMore()");
        if (gameListFilterType == FAVOURITES) {
          onLoadMoreScrollListener.notifyDataLoaded();
        } else {
          viewModel.requestForPaginationGameList();
        }
      }
    };

    binding.recyclerView.setLayoutManager(linearLayoutManager);
    binding.recyclerView.setAdapter(gamesAdapter);
    binding.recyclerView.addOnScrollListener(onLoadMoreScrollListener);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_game_list, menu);
    Timber.d("Current GameListFilterType: %s", gameListFilterType);

    MenuItem menuItem = null;
    switch (gameListFilterType) {
      case POPULARITY:
        menuItem = menu.findItem(R.id.filter_popularity);
        break;
      case TOP_RATED:
        menuItem = menu.findItem(R.id.filter_top_rated);
        break;
      case COMING_SOON:
        menuItem = menu.findItem(R.id.filter_coming_soon);
        break;
      case FAVOURITES:
        menuItem = menu.findItem(R.id.filter_favourites);
        break;
    }
    if (menuItem != null) {
      menuItem.setChecked(true);
    }

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.filter_popularity:
      case R.id.filter_top_rated:
      case R.id.filter_coming_soon:
      case R.id.filter_favourites:
        item.setChecked(!item.isChecked());
        if (item.getItemId() == R.id.filter_popularity) {
          viewModel.setGameListFilterType(GameListFilterType.POPULARITY);
        } else if (item.getItemId() == R.id.filter_top_rated) {
          viewModel.setGameListFilterType(GameListFilterType.TOP_RATED);
        } else if (item.getItemId() == R.id.filter_coming_soon) {
          viewModel.setGameListFilterType(GameListFilterType.COMING_SOON);
        } else if (item.getItemId() == R.id.filter_favourites) {
          viewModel.setGameListFilterType(FAVOURITES);
        }
        return true;
      case R.id.action_search:
        Intent searchIntent = new Intent(this, GameSearchActivity.class);
        startActivity(searchIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
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

  public void scheduleJob() {
    ComponentName componentName = new ComponentName(this, JobSchedulerService.class);
    JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
    builder.setRequiresDeviceIdle(false);
    builder.setRequiresCharging(false);
    builder.setPersisted(true);
    builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
    builder.setPeriodic(REFRESH_INTERVAL);

    JobInfo jobInfo = builder.build();

    JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

    Timber.d(jobScheduler.toString());
    Timber.d("Scheduling a Job...");
    Timber.d(jobInfo.toString());
    int ret = jobScheduler.schedule(jobInfo);
    if (ret == JobScheduler.RESULT_SUCCESS) {
      Timber.d("Job scheduled successfully.");
    } else {
      Timber.d("Failed to schedule job.");
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (gameListFilterType == FAVOURITES
        && requestCode == REQUEST_CODE
        && resultCode == RESULT_OK
        && data != null) {
      int adapterPosition =
          data.getIntExtra(GameDetailsActivity.EXTRA_ADAPTER_POSITION, NO_POSITION);
      boolean isGameRemovedFromFavourite =
          data.getBooleanExtra(GameDetailsActivity.EXTRA_ITEM_REMOVED, false);
      Timber.d("adapterPosition: %d, isGameRemovedFromFavourite: %s", adapterPosition,
          isGameRemovedFromFavourite);
      if (adapterPosition != NO_POSITION && isGameRemovedFromFavourite) {
        viewModel.gameRemovedFromFavourites(adapterPosition);
      }
    }
  }

  private void loadNativeAds() {

    AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_ad_unit_id));
    adLoader = builder.forUnifiedNativeAd(
        unifiedNativeAd -> {
          nativeAds.add(unifiedNativeAd);
          if (!adLoader.isLoading()) {
            viewModel.setNativeAdList(nativeAds);
          }
          Timber.d("A native ad loaded successfully. Still loading: %s", adLoader.isLoading());
        }).withAdListener(new AdListener() {
      @Override
      public void onAdFailedToLoad(int errorCode) {
        Timber.e("The previous native ad failed to load. Attempting to load another.");
        if (!adLoader.isLoading()) {
          viewModel.setNativeAdList(nativeAds);
        }
      }
    }).build();

    // Load the Native ads.
    adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
  }
}
