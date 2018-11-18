package me.bloodybadboy.gamedatabase.data.source.local.db.entity.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.bloodybadboy.gamedatabase.data.model.Image;
import me.bloodybadboy.gamedatabase.data.model.Pulse;
import me.bloodybadboy.gamedatabase.data.source.local.db.entity.PulseEntity;

public class PulseEntityDataMapper {

  public PulseEntityDataMapper() {

  }

  public Pulse transform(PulseEntity pulseEntity) {
    Pulse pulse = null;
    if (pulseEntity != null) {
      pulse = new Pulse();

      Image pulseImage = new Image();
      pulseImage.setCloudinaryId(pulseEntity.getImageCloudinaryId());

      pulse.setId(pulseEntity.getId());
      pulse.setTitle(pulseEntity.getTitle());
      pulse.setPulseImage(pulseImage);
      pulse.setPublishedAt(pulseEntity.getPublishedAt());
      pulse.setAuthor(pulseEntity.getAuthor());
      pulse.setUrl(pulseEntity.getUrl());
    }
    return pulse;
  }

  public PulseEntity transform(Pulse pulse) {
    PulseEntity pulseEntity = null;
    if (pulse != null) {
      pulseEntity = new PulseEntity();

      Image pulseImage = pulse.getPulseImage();
      String pulseImageCloudinaryId = null;
      if (pulseImage != null) {
        pulseImageCloudinaryId = pulseImage.getCloudinaryId();
      }

      pulseEntity.setId(pulse.getId());
      pulseEntity.setTitle(pulse.getTitle());
      pulseEntity.setImageCloudinaryId(pulseImageCloudinaryId);
      pulseEntity.setPublishedAt(pulse.getPublishedAt());
      pulseEntity.setAuthor(pulse.getAuthor());
      pulseEntity.setUrl(pulse.getUrl());
    }

    return pulseEntity;
  }

  public List<Pulse> transformToPulse(Collection<PulseEntity> pulseEntityCollection) {
    final List<Pulse> pulseList = new ArrayList<>();
    for (PulseEntity pulseEntity : pulseEntityCollection) {
      final Pulse pulse = transform(pulseEntity);
      if (pulse != null) {
        pulseList.add(pulse);
      }
    }
    return pulseList;
  }

  public List<PulseEntity> transformToEntity(Collection<Pulse> pulseCollection) {
    final List<PulseEntity> pulseEntityList = new ArrayList<>();
    for (Pulse pulse : pulseCollection) {
      final PulseEntity pulseEntity = transform(pulse);
      if (pulseEntity != null) {
        pulseEntityList.add(pulseEntity);
      }
    }
    return pulseEntityList;
  }
}
