package com.mumtaz.reactiveorchestrate.controller;

import com.mumtaz.reactiveorchestrate.domain.Car;
import com.mumtaz.reactiveorchestrate.domain.Location;
import com.mumtaz.reactiveorchestrate.domain.LocationGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BookingController {

    private final WebClient carsClient = WebClient.create("http://localhost:8080");
    private final WebClient bookClient = WebClient.create("http://localhost:8080");

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    @PostMapping("/booking")
    public Mono<ResponseEntity<String>> book() {
        logger.debug("Processing booking request");

        carsClient.get().uri("/cars")
                .retrieve()
                .bodyToFlux(Car.class)
                .doOnNext(car -> logger.debug("Trying to book car {}", car))
                .take(5)
                .flatMap(this::requestCar)
                .next()
                .doOnNext(car -> logger.debug("Booked car {}", car));

        return Mono.just(new ResponseEntity<String>( "dummy", HttpStatus.OK));
    }

    private Mono<ResponseEntity<Void>> requestCar(Car car) {
        return bookClient.post()
                .uri("/cars/{id}/booking", car.getId())
                .exchange()
                .flatMap(response -> response.toEntity(Void.class));
    }

    @GetMapping("/testMono")
    public Mono<ResponseEntity<Void>> testBookingMono() {
        LocationGenerator locationGenerator = new LocationGenerator(34L, 34L);
        Car car = new Car(123L, locationGenerator.location());
        return bookClient.post()
                .uri("/cars/123/booking")
                .body(BodyInserters.fromObject(car))
                .exchange()
                .flatMap(response -> {
                    System.out.println(response);
                    return response.toEntity(Void.class);
                } );

    }
}


