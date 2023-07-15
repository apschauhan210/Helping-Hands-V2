package com.hh.helping_hands_rs.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Arrays;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Address {

    private String location;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{
                location.hashCode(),
                city.hashCode(),
                state.hashCode(),
                country.hashCode(),
                zipCode.hashCode()
        });
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if (obj instanceof Address e) {
            return (
                    location.equals(e.location) &&
                    city.equals(e.city) &&
                    state.equals(e.state) &&
                    country.equals(e.country) &&
                    zipCode.equals(e.zipCode)
            );
        }
        return false;
    }
}
