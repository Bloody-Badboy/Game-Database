package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.List;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.domain.usecase.base.UseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public class RemoveNewsFromDatabaseUseCase extends UseCase<Object> {

  private final GameDataRepository repository;
  private List<Pulse> pulseList;

  @Inject RemoveNewsFromDatabaseUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;
  }

  public RemoveNewsFromDatabaseUseCase setPulseList(List<Pulse> pulseList) {
    this.pulseList = pulseList;
    return this;
  }

  @Override protected Result<Object> execute() {
    repository.removeNewsFromDatabase(pulseList);
    return Result.Success(new Object());
  }
}
