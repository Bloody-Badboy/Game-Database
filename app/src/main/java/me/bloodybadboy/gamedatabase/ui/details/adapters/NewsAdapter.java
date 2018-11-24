package me.bloodybadboy.gamedatabase.ui.details.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.gamedatabase.R;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.databinding.ListItemNewsLayoutBinding;
import me.bloodybadboy.gamedatabase.utils.ChromeTabsUtils;
import me.bloodybadboy.gamedatabase.utils.binding.BindableAdapter;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements
    BindableAdapter<List<Pulse>> {

  private final List<Pulse> pulses = new ArrayList<>();

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new ViewHolder(
        DataBindingUtil.inflate(inflater, R.layout.list_item_news_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    bindViewHolder(holder, pulses.get(position));
  }

  @Override
  public int getItemCount() {
    return pulses.size();
  }

  private void bindViewHolder(RecyclerView.ViewHolder viewHolder, Pulse pulse) {
    ViewHolder videoListItemViewHolder = (ViewHolder) viewHolder;
    if (pulse != null) {
      videoListItemViewHolder.binding.setPulse(pulse);
      videoListItemViewHolder.binding.getRoot().setTag(pulse);
    }
  }

  public void setPulseList(List<Pulse> pulses) {
    this.pulses.addAll(pulses);
    notifyDataSetChanged();
  }

  @Override public void setData(List<Pulse> data) {
    pulses.addAll(data);
    notifyDataSetChanged();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ListItemNewsLayoutBinding binding;

    ViewHolder(ListItemNewsLayoutBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      binding.getRoot().setOnClickListener(v -> {
        Pulse pulse = (Pulse) v.getTag();
        showArticleInCustomTab(binding.getRoot().getContext(), pulse.getUrl());
      });
    }

    void showArticleInCustomTab(Context context, String url) {
      if (url == null) {
        Toast.makeText(context, "Can't show news article.", Toast.LENGTH_SHORT).show();
      } else {
        ChromeTabsUtils.open(context, url);
      }
    }
  }
}
