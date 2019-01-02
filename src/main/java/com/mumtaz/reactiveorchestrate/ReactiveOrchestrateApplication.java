package com.mumtaz.reactiveorchestrate;

import com.mumtaz.reactiveorchestrate.domain.Car;
import com.mumtaz.reactiveorchestrate.domain.LocationGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
public class ReactiveOrchestrateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveOrchestrateApplication.class, args);
	}


//	@Bean
//	CommandLineRunner populateMongoCars (MongoOperations mongo) {
//		return (String... args) -> {
//			mongo.dropCollection(Car.class);
//			mongo.createCollection(Car.class, CollectionOptions.empty().size(10000).capped());
//
//			LocationGenerator locationGenerator = new LocationGenerator(33,33);
//			Flux.range(1, 100)
//					.map(i -> new Car(i.longValue(), locationGenerator.location()))
//					.doOnNext(mongo::save)
//					.log()
//					.blockLast(Duration.ofSeconds(5));
//		};
//	}
}

