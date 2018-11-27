package me.bloodybadboy.gamedatabase.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.databinding.ActivityGameSearchBinding;
import me.bloodybadboy.gamedatabase.ui.search.adapter.GamesSearchAdapter;
import me.bloodybadboy.gamedatabase.utils.NetworkUtil;
import me.bloodybadboy.gamedatabase.utils.TextWatcherAdapter;
import me.bloodybadboy.gamedatabase.utils.event.EventObserver;

public class GameSearchActivity extends DaggerAppCompatActivity
    implements EditText.OnEditorActionListener {

  @Inject
  ViewModelProvider.Factory viewModelFactory;
  private int WAITING_TIME = 300;
  private ActivityGameSearchBinding binding;
  private GameSearchViewModel viewModel;
  private Handler handler = new Handler();
  private GamesSearchAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_game_search);
    //viewModel = obtainViewModel(this);
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameSearchViewModel.class);

    binding.setLifecycleOwner(this);
    binding.setViewModel(viewModel);

    binding.btnClearSearch.setOnClickListener(v -> binding.etSearch.setText(null));
    binding.btnBackArrow.setOnClickListener(v -> finish());

    binding.etSearch.setOnEditorActionListener(this);
    binding.etSearch.setFocusable(true);
    binding.etSearch.requestFocus();
    binding.etSearch.addTextChangedListener(new TextWatcherAdapter() {
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (binding.etSearch.getText().toString().equals("")) {
          binding.btnClearSearch.setVisibility(View.GONE);
        } else {
          binding.btnClearSearch.setVisibility(View.VISIBLE);
        }
        if (binding.etSearch.getText().toString().length() < 3) {
          return;
        }

        handler.removeCallbacksAndMessages(null);

        handler.postDelayed(() -> viewModel.searchForGame(binding.etSearch.getText().toString()),
            WAITING_TIME);
      }
    });

    if (binding.etSearch.requestFocus() && binding.etSearch.getText().toString().equals("")) {
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    adapter = new GamesSearchAdapter(this);

    binding.recyclerView.setLayoutManager(
        new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    binding.recyclerView.setHasFixedSize(true);
    binding.recyclerView.setAdapter(adapter);

    viewModel.gameList.observe(this, games -> adapter.setData(games));

    viewModel.exceptionEvent.observe(this, exceptionEvent -> viewModel.showRetrySnackBar(
        NetworkUtil.isOnline() ? getString(R.string.error_msg_unexpected)
            : getString(R.string.error_msg_no_internet)));

    viewModel.showRetrySnackBarEvent.observe(this, new EventObserver<>(this::showErrorSnackbar));
  }

  @Override protected void onResume() {
    super.onResume();

    if (!binding.etSearch.getText().toString().equals("")) {
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
  }

  @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

    if (!binding.etSearch.getText().toString().equals("")) {
      viewModel.searchForGame(binding.etSearch.getText().toString());
    }

    View view = this.getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    return true;
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
}
