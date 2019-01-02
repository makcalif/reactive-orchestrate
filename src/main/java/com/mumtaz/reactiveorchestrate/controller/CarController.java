package com.mumtaz.reactiveorchestrate.controller;

import com.mumtaz.reactiveorchestrate.domain.Booking;
import com.mumtaz.reactiveorchestrate.domain.Car;
import com.mumtaz.reactiveorchestrate.domain.Location;
import com.mumtaz.reactiveorchestrate.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@RestController
public class CarController {
    Logger logger = LoggerFactory.getLogger(CarController.class);
    CarRepository carRepository;

    @Autowired
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping(value = "/cars", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Car> getAllCars() {
        return this.carRepository.findCarBy().log();
    }

    // TODO use a Mono as input to the end point for stream
    @PostMapping(value = "cars/{id}/booking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Booking> createBooking (@RequestBody Car car) {
        logger.debug("start booking....");
        Mono<Long> delay = Mono.delay(Duration.ofSeconds(5)).log();
        Location location = new Location();
        Mono<Booking> bookingMono = Mono.just(new Booking(123L, new Date(), car, location)).log();
        Mono<Booking> bookingWithDelay =
                bookingMono.
                        zipWith(delay.log(), (booking, longDelay) -> booking);
        logger.debug("booking complete");
        return bookingWithDelay;
    }
}
