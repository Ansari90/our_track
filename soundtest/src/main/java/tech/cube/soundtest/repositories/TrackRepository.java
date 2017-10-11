package tech.cube.soundtest.repositories;

import org.springframework.data.repository.CrudRepository;

import tech.cube.soundtest.entities.Track;

public interface TrackRepository extends CrudRepository<Track, Long> {

}
