package com.mumtaz.reactiveorchestrate.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    Long bookingId;
    Date bookingTime;
    Car car;
    Location location;

}
