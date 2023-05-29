package com.hh.helping_hands_rs.controllers;

import com.hh.helping_hands_rs.dtos.EmployerDto;
import com.hh.helping_hands_rs.dtos.HelperDto;
import com.hh.helping_hands_rs.entities.Employer;
import com.hh.helping_hands_rs.entities.Job;
import com.hh.helping_hands_rs.services.EmployerService;
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
@RequestMapping("/employer")
@AllArgsConstructor
public class EmployerController {

    private final EmployerService employerService;

    private final static Logger logger = LoggerFactory.getLogger(EmployerController.class);

    @GetMapping()
    public ResponseEntity getHelperByEmail(@RequestParam String email) {
        try {
            Employer employer = employerService.findEmployerByEmail(email);
            EmployerDto employerDto = new EmployerDto(
                    employer.getEmail(),
                    employer.getName(),
                    employer.getMobileNumber()
            );
            return new ResponseEntity(employerDto, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/requested-helpers")
    @PreAuthorize("hasRole(@Role.EMPLOYER)")
    public ResponseEntity getRequestedHelpers(Authentication authentication) {
        try {
            String email = authentication.getName();
            Employer employer = employerService.findEmployerByEmail(email);
            Set<HelperDto> helpers =
                    employer.getRequestedHelpers()
                            .stream().map((helper) -> new HelperDto(
                                    helper.getEmail(),
                                    helper.getName(),
                                    helper.getMobileNumber(),
                                    helper.getAddressToWork(),
                                    helper.getJobs().stream().map(Job::getName).collect(Collectors.toSet())
                            )).collect(Collectors.toSet());
            return new ResponseEntity(helpers, HttpStatus.OK);
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
