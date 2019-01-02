package com.mumtaz.reactiveorchestrate.controller;

import com.mumtaz.reactiveorchestrate.domain.Booking;
import com.mumtaz.reactiveorchestrate.domain.Car;
import com.mumtaz.reactiveorchestrate.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.time.Duration;
import java.util.Date;

@RestController
public class CarController {

    CarRepository carRepository;

    @Autowired
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping(value = "/cars", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Car> getAllCars() {
        return this.carRepository.findCarBy().log();
    }

    @PostMapping(value = "cars/{id}/booking")
    public Mono<Booking> createBooking (@RequestBody Car car) {
        Mono<Long> delay = Mono.delay(Duration.ofSeconds(5));
        Mono<Booking> bookingMono = Mono.just(new Booking(123L, new Date()));
        Mono<Booking> bookingWithDelay =
                bookingMono.zipWith(delay, (booking, longDelay) -> booking);
        return bookingWithDelay;
    }
}
