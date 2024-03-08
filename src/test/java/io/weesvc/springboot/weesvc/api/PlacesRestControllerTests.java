package io.weesvc.springboot.weesvc.api;

import io.weesvc.springboot.weesvc.WeesvcApplication;
import io.weesvc.springboot.weesvc.domain.PlacesRepository;
import io.weesvc.springboot.weesvc.domain.model.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = { WeesvcApplication.class, PlacesRestController.class, PlacesRepository.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.r2dbc.url=r2dbc:tc:postgresql:///testdb?TC_IMAGE_TAG=15.3-alpine" }
)
public class PlacesRestControllerTests {

    private static final Duration ONE_SECOND_DURATION = Duration.ofSeconds(1);

    @Autowired
    PlacesRestController fixture;

    @Test
    void getPlaces() {
        final List<Place> places =
                fixture.getPlaces().collectList().block(ONE_SECOND_DURATION);
        assertTrue(places.size() > 0, "Expecting a non-empty list of places");
    }

    @Test
    void getPlaceByID() {
        final Place place = fixture.getPlaceById(6L).block(ONE_SECOND_DURATION);
        assertNotNull(place, "Expecting seeded place with id=6");
        assertEquals(6L, place.getId());
        assertEquals("MIA", place.getName());
        assertEquals("Miami International Airport, FL, USA", place.getDescription());
        assertEquals(25.79516F, place.getLatitude());
        assertEquals(-80.27959F, place.getLongitude());
        assertNotNull(place.getCreatedAt(), "Created date should not be nullable");
        assertNotNull(place.getUpdatedAt(), "Updated date should not be nullable");
    }

    @Test
    void createPlace() {
        final Place newPlace = Place.builder()
                .name("Kerid Crater")
                .description("Kerid Crater, Iceland")
                .latitude(64.04126F)
                .longitude(-20.88530F)
                .build();

        final Place created = fixture.addPlace(newPlace).block();
        assertNotNull(created.getId(), "ID should be autoincremented");
        assertEquals(newPlace.getName(), created.getName());
        assertEquals(newPlace.getDescription(), created.getDescription());
        assertEquals(newPlace.getLatitude(), created.getLatitude());
        assertEquals(newPlace.getLongitude(), created.getLongitude());
        assertNotNull(created.getCreatedAt(), "Created date should not be nullable");
        assertNotNull(created.getUpdatedAt(), "Updated date should not be nullable");
    }

    @Test
    void updatePlaceByID() {
        final Place original = fixture.getPlaceById(7L).block(ONE_SECOND_DURATION);
        assertNotNull(original, "Expecting seeded place with id=7");

        final Place changes = original.toBuilder()
                .name("The Alamo")
                .description("The Alamo, San Antonio, TX, USA")
                .latitude(29.42590F)
                .longitude(-98.48625F)
                .build();
        final Place updated = fixture.updatePlaceById(original.getId(), changes).block(ONE_SECOND_DURATION);
        assertEquals(changes.getId(), updated.getId());
        assertEquals(changes.getName(), updated.getName());
        assertEquals(changes.getDescription(), updated.getDescription());
        assertEquals(changes.getLatitude(), updated.getLatitude());
        assertEquals(changes.getLongitude(), updated.getLongitude());
        assertEquals(changes.getCreatedAt(), updated.getCreatedAt());
        assertNotEquals(changes.getUpdatedAt(), updated.getUpdatedAt(), "Updated date should be changed");
    }

    @Test
    void deletePlaceByID() {
        final Long deleteID = 1L;
        fixture.deletePlaceById(deleteID).block(ONE_SECOND_DURATION);

        Exception e = assertThrows(RuntimeException.class, () -> {
            fixture.getPlaceById(deleteID).block(ONE_SECOND_DURATION);
        });
        assertEquals(PlaceNotFoundException.class, e.getCause().getClass());
    }

}
