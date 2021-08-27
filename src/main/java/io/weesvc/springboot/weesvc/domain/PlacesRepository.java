package io.weesvc.springboot.weesvc.domain;

import io.weesvc.springboot.weesvc.domain.model.Place;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlacesRepository extends ReactiveCrudRepository<Place, Long> {

}
