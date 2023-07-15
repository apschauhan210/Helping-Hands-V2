package com.hh.helping_hands_rs.controllers;

import com.hh.helping_hands_rs.dtos.EmployerDto;
import com.hh.helping_hands_rs.dtos.HelperDto;
import com.hh.helping_hands_rs.dtos.LocationAndJobsDto;
import com.hh.helping_hands_rs.entities.Helper;
import com.hh.helping_hands_rs.entities.Job;
import com.hh.helping_hands_rs.models.Address;
import com.hh.helping_hands_rs.services.HelperService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/helper")
@AllArgsConstructor
public class HelperController {

    private final HelperService helperService;

    private final static Logger logger = LoggerFactory.getLogger(HelperController.class);

    @GetMapping
    @PreAuthorize("hasAnyRole(@Role.EMPLOYER, @Role.HELPER)")
    public ResponseEntity getHelperByEmail(@RequestParam String email) {
        try {
            Helper helper = helperService.findHelperByEmail(email);
            HelperDto helperDto = new HelperDto(
                    helper.getEmail(),
                    helper.getName(),
                    helper.getMobileNumber(),
                    helper.getAddressToWork(),
                    helper.getJobs().stream().map(Job::getName).collect(Collectors.toSet())
            );
            return new ResponseEntity<>(helperDto, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/offering-employers")
    @PreAuthorize("hasRole(@Role.HELPER)")
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
                                            employer.getMobileNumber()
                                    )).collect(Collectors.toSet());
            return new ResponseEntity<>(employers, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-working-location")
    @PreAuthorize("hasRole(@Role.HELPER)")
    public ResponseEntity<HttpStatus> addHelperWorkingLocation(@RequestBody Address address, Authentication authentication) {
        try {
            helperService.addHelpersWorkingLocation(authentication.getName(), address);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/helpers-by-location")
    @PreAuthorize("hasRole(@Role.EMPLOYER)")
    public ResponseEntity<List<HelperDto>> getHelpersByLocation(@RequestBody Address address) {
        try {
            Set<Helper> helpers = helperService.getHelperByLocation(address);
            return new ResponseEntity<>(convertHelperSetToHelperDTOList(helpers), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/working-locations")
    @PreAuthorize("hasRole(@Role.HELPER)")
    public ResponseEntity getWorkingLocations(Authentication authentication) {
        try {
            String email = authentication.getName();
            return new ResponseEntity<>(helperService.getWorkingLocations(email), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-jobs")
    @PreAuthorize("hasRole(@Role.HELPER)")
    public ResponseEntity<HttpStatus> addJobs(@RequestBody List<String> jobs, Authentication authentication) {
        try {
            String email = authentication.getName();
            helperService.addHelpersJob(email, jobs);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-jobs")
    @PreAuthorize("hasRole(@Role.HELPER)")
    public ResponseEntity<List<String>> getJobs(Authentication authentication) {
        try {
            String email = authentication.getName();
            return new ResponseEntity<>(helperService.getHelpersJob(email), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-offering-employer")
    @PreAuthorize("hasRole(@Role.EMPLOYER)")
    public ResponseEntity addOfferingEmployer(@RequestParam String helperEmail, Authentication authentication) {
        try {
            String employerEmail = authentication.getName();
            helperService.addOfferingEmployer(helperEmail, employerEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/helpers-by-location-and-jobs")
    @PreAuthorize("hasRole(@Role.EMPLOYER)")
    public ResponseEntity<List<HelperDto>> getHelpersByLocationAndJobs(@RequestBody LocationAndJobsDto requestBody) {
        try {
            if(requestBody.address == null || requestBody.job == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Set<Helper> helperSet = helperService.getHelpersByLocationAndJobs(requestBody.address, requestBody.job);
            return new ResponseEntity<>(convertHelperSetToHelperDTOList(helperSet), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<HelperDto> convertHelperSetToHelperDTOList(Set<Helper> helperSet) {
        return helperSet.stream()
                .map(helper -> new HelperDto(
                        helper.getEmail(),
                        helper.getName(),
                        helper.getMobileNumber(),
                        helper.getAddressToWork(),
                        helper.getJobs().stream().map(Job::getName).collect(Collectors.toSet())
                )).toList();
    }
}
