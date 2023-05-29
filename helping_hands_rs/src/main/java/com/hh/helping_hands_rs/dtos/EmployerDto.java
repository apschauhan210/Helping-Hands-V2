package com.hh.helping_hands_rs.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class EmployerDto {

    private String email;
    private String name;
    private String mobileNumber;
//    private Set<HelperDto> requestedHelpers;
}
