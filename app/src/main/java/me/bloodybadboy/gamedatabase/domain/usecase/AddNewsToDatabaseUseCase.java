package me.bloodybadboy.gamedatabase.domain.usecase;

import java.util.List;
import javax.inject.Inject;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.data.source.GameDataRepository;
import me.bloodybadboy.gamedatabase.domain.usecase.base.UseCase;
import me.bloodybadboy.gamedatabase.result.Result;
import me.bloodybadboy.gamedatabase.utils.scheduler.Scheduler;

public class AddNewsToDatabaseUseCase extends UseCase<Object> {

  private final GameDataRepository repository;
  private List<Pulse> pulseList;

  @Inject AddNewsToDatabaseUseCase(Scheduler scheduler, GameDataRepository repository) {
    super(scheduler);
    this.repository = repository;
  }

  public AddNewsToDatabaseUseCase setPulseList(List<Pulse> pulseList) {
    this.pulseList = pulseList;
    return this;
  }

  @Override protected Result<Object> execute() {
    repository.addNewsToDatabase(pulseList);
    return Result.Success(new Object());
  }
}
