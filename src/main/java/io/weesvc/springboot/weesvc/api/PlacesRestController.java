package io.weesvc.springboot.weesvc.api;

import io.weesvc.springboot.weesvc.domain.PlacesRepository;
import io.weesvc.springboot.weesvc.domain.model.Place;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/api/places")
@Slf4j
public class PlacesRestController {

    @Autowired
    PlacesRepository placesRepository;

    @GetMapping
    public Flux<Place> getPlaces() {
        return placesRepository.findAll();
    }

    @PostMapping
    public Mono<Place> addPlace(@RequestBody Place place) {
        log.info("Adding place named {}", place.getName());
        final Instant now = Instant.now();
        place.setCreatedAt(now);
        place.setUpdatedAt(now);
        return placesRepository.save(place);
    }

    @GetMapping("{id}")
    public Mono<Place> getPlaceById(@PathVariable Long id) {
        log.info("Retrieving place with ID {}", id);
        return placesRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlaceNotFoundException()));
    }

    @PatchMapping("{id}")
    public Mono<Place> updatePlaceById(@PathVariable Long id,
                                       @RequestBody Place updates) {
        log.info("Updating place with ID {}", id);
        return placesRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlaceNotFoundException()))
                .flatMap(p -> {
                    if (updates.getName() != null) {
                        p.setName(updates.getName());
                    }
                    if (updates.getDescription() != null) {
                        p.setDescription(updates.getDescription());
                    }
                    if (updates.getLatitude() != null) {
                        p.setLatitude(updates.getLatitude());
                    }
                    if (updates.getLongitude() != null) {
                        p.setLongitude(updates.getLongitude());
                    }
                    p.setUpdatedAt(Instant.now());

                    return placesRepository.save(p);
                });
    }

    @DeleteMapping("{id}")
    public Mono<Void> deletePlaceById(@PathVariable Long id) {
        log.info("Deleting place with ID {}", id);
        return placesRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlaceNotFoundException()))
                .then(placesRepository.deleteById(id));
    }

}
