package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.Calendar;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public final class GetComingSoonGameListUseCase extends AbstractGetGameListUseCase {

  public GetComingSoonGameListUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler, repository);
  }

  @Override void addOrderParameter() {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MONTH, 12);

    long time = calendar.getTimeInMillis();

    parameters.addOrder("first_release_date:desc");
    parameters.addFilter("[first_release_date][gt]",
        String.valueOf(Calendar.getInstance().getTimeInMillis()));
    parameters.addFilter("[first_release_date][lte]",
        String.valueOf(time));
  }
}
