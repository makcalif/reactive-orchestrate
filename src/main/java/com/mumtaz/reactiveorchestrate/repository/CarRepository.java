package com.mumtaz.reactiveorchestrate.repository;

import com.mumtaz.reactiveorchestrate.domain.Car;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface CarRepository extends ReactiveMongoRepository<Car, Long> {

    @Tailable
    Flux<Car> findCarBy();
}
