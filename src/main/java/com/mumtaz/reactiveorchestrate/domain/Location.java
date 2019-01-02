package com.mumtaz.reactiveorchestrate.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Location {
    private BigDecimal longitude;

    private BigDecimal latitude;

//    public Location(BigDecimal longitude, BigDecimal latitude) {
//        this.longitude = longitude;
//        this.latitude = latitude;
//    }
}
