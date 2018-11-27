package me.bloodybadboy.gamedatabase.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.source.remote.api.IGDBParameters;
import me.bloodybadboy.gamedatabase.domain.usecase.GetFavouriteGamesNewsUseCase;
import me.bloodybadboy.gamedatabase.domain.usecase.UpdateNewsDatabaseUseCase;
import timber.log.Timber;

public class JobSchedulerService extends JobService {

  private final IGDBParameters parameters = new IGDBParameters();

  @Inject
  public GetFavouriteGamesNewsUseCase favouriteGamesNewsUseCase;

  @Inject
  public UpdateNewsDatabaseUseCase updateNewsDatabaseUseCase;

  @Override public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();

    parameters.addField("title")
        .addField("published_at")
        .addField("pulse_image.cloudinary_id")
        .addField("url")
        .addField("author")
        .addOrder("published_at:desc")
        .addLimit(30);
  }

  private void syncDataToLocalDatabase(final JobParameters params) {
    favouriteGamesNewsUseCase.setListener(pulseResult -> {
      if (!pulseResult.loading()) {
        if (pulseResult.succeeded()) {
          if (pulseResult.data.size() > 0) {
            updateNewsDatabaseUseCase.setPulseList(pulseResult.data)
                .setListener(result -> {
                  if (!result.loading()) {
                    Timber.d("Job finished successfully.");
                    jobFinished(params, false);
                  }
                }).executeAsync();
          } else {
            Timber.e(
                "Pulses list is empty, Maybe games are added to favourites, Rescheduling the Job.");
            jobFinished(params, true);
          }
        } else {
          Timber.e(pulseResult.exception, "Job finished with error, Rescheduling the Job.");
          jobFinished(params, true);
        }
      }
    }).executeAsync();
  }

  @Override public boolean onStartJob(JobParameters params) {
    Timber.d("Job Started.");
    syncDataToLocalDatabase(params);
    return true;
  }

  @Override public boolean onStopJob(JobParameters params) {
    Timber.d("Job Stopped.");
    return true;
  }
}
