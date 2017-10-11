package tech.cube.soundtest.repositories;

import org.springframework.data.repository.CrudRepository;
import tech.cube.soundtest.entities.TrackList;

public interface TrackListRepository extends CrudRepository<TrackList, Long> {
}
