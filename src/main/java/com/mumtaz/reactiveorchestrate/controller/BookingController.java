package com.mumtaz.reactiveorchestrate.controller;

import com.mumtaz.reactiveorchestrate.domain.Car;
import com.mumtaz.reactiveorchestrate.domain.Location;
import com.mumtaz.reactiveorchestrate.domain.LocationGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

@RestController
public class BookingController {
    Logger logger = LoggerFactory.getLogger(BookingController.class);
    private final WebClient carsClient = WebClient.create("http://localhost:8080");
    private final WebClient bookClient = WebClient.create("http://localhost:8080");

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

    @GetMapping(value = "/testMono", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<String>> testBookingMono() {
        LocationGenerator locationGenerator = new LocationGenerator(34L, 34L);
        Car car = new Car(123L, locationGenerator.location());
        logger.debug("========== client booking start ...");
        return bookClient.post()
            .uri("/cars/123/booking")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .body(BodyInserters.fromObject(car))
            .exchange()
            .log()
            .flatMap(response -> {
                // When you flatmap you're basically "merging" all the publishers
                // from the map into a single Mono
                if(response.statusCode().is4xxClientError()) {
                    response.body((clientHttpResponse, context) -> {
                        return Mono.just( new ResponseEntity<String>(clientHttpResponse.getBody().toString(), HttpStatus.OK));
                    });
                }
                return response.toEntity(String.class);
            } ) ;
    }

    // sample method simpulate a simple flux stream
    //  curl -i http://localhost:8080/testFlux -N
    @GetMapping(value = "/testFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> testBookingFlux() {
        Flux  data = Flux.fromStream(Stream.generate(() -> {
            return System.currentTimeMillis();
        }));

        Flux delay = Flux.interval(Duration.ofSeconds(2));

        Flux delayedData = data.zipWith(delay, (dta, dely) -> dta);
        return delayedData;
    }

    @GetMapping(value = "/bookmulti")
    public Mono<ResponseEntity<Void>> testBookingMulti() {
        return carsClient.get()
                .uri("/cars")
                .retrieve()
                .bodyToFlux(Car.class)
                .doOnNext(car -> logger.debug("trying to book car : {}", car))
                .take(2)
                .flatMap(this::requestCar)
                .next()
                .doOnNext(car -> logger.debug("booked car {}", car));
    }

    private Mono<ResponseEntity<Void>> requestCar(Car car) {
        return bookClient.post()
                .uri("/cars/{id}/booking", car.getId())
                .exchange()
                .flatMap(response -> response.toEntity(Void.class));
    }
}


