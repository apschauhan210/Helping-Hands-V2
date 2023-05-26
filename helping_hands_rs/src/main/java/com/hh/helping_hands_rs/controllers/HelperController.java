package com.hh.helping_hands_rs.controllers;

import com.hh.helping_hands_rs.dtos.EmployerDto;
import com.hh.helping_hands_rs.dtos.HelperDto;
import com.hh.helping_hands_rs.entities.Helper;
import com.hh.helping_hands_rs.services.HelperService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/helper")
@AllArgsConstructor
public class HelperController {

    private final HelperService helperService;

    private final static Logger logger = LoggerFactory.getLogger(HelperController.class);

    @GetMapping
    public ResponseEntity getHelperByEmail(@RequestParam String email) {
        try {
            Helper helper = helperService.findHelperByEmail(email);
            HelperDto helperDto = new HelperDto(
                    helper.getEmail(),
                    helper.getName(),
                    helper.getMobileNumber(),
                    helper.getAddressToWork(),
                    null,
                    helper.getJobs()
            );
            return new ResponseEntity(helperDto, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/offering-employers")
    @PreAuthorize("hasRole('helper')")
    public ResponseEntity getOfferingEmployers(Authentication authentication) {
        try {
            String email = authentication.getName();
            Helper helper = helperService.findHelperByEmail(email);
            Set<EmployerDto> employers =
                    helper.getOfferingEmployers()
                            .stream().map((employer) ->
                                new EmployerDto(
                                    employer.getEmail(),
                                    employer.getName(),
                                    employer.getMobileNumber(),
                                    null
                            )).collect(Collectors.toSet());
            return new ResponseEntity(employers, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
