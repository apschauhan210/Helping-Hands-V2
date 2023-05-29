package com.hh.helping_hands_rs.dtos;

import com.hh.helping_hands_rs.entities.Job;
import com.hh.helping_hands_rs.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class HelperDto {

    private String email;
    private String name;
    private String mobileNumber;
    private Set<Address> addressToWork;
//    private Set<EmployerDto> offeringEmployers;
    private Set<String> jobs;
}
