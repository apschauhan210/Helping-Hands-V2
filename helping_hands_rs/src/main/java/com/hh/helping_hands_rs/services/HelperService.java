package com.hh.helping_hands_rs.services;

import com.hh.helping_hands_rs.entities.Employer;
import com.hh.helping_hands_rs.entities.Helper;
import com.hh.helping_hands_rs.entities.Job;
import com.hh.helping_hands_rs.models.Address;
import com.hh.helping_hands_rs.repositories.HelperRepository;
import com.hh.helping_hands_rs.repositories.JobRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class HelperService {

    private final HelperRepository helperRepository;

    private final EmployerService employerService;

    private final JobRepository jobRepository;

    public Helper findHelperByEmail(String email) {
        Optional<Helper> helperOptional = helperRepository.findHelperByEmail(email);
        return helperOptional.orElseThrow(() -> new UsernameNotFoundException(email + " is not registered"));
    }

    public void registerHelper(Authentication authentication) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = principal.getClaims();
        String name = claims.get("fname") + " " + claims.get("lname");
        Helper helper = new Helper(
                authentication.getName(),   // email(username) is the name in authentication object
                name,
                null,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
        Optional<Helper> helperByEmail = helperRepository.findHelperByEmail(helper.getEmail());
        if (helperByEmail.isEmpty())
            helperRepository.save(helper);
    }

    @Transactional
    public void addHelpersWorkingLocation(String email, Address address) {
        Helper helper = this.findHelperByEmail(email);

        if (helper.getAddressToWork() == null) {
            helper.setAddressToWork(new HashSet<>());
        }

        helper.getAddressToWork().add(address);
    }

    public Set<Helper> getHelperByLocation(Address address) {
        Set<Helper> resHelpers = new LinkedHashSet<>();

        helperRepository.findHelpersByAddressToWork(address)
                .ifPresent(resHelpers::addAll);

        helperRepository.findHelpersByAddressToWorkExcludingLocation(address)
                .ifPresent(resHelpers::addAll);

        helperRepository.findHelpersByAddressToWorkWithCityAndState(address)
                .ifPresent(resHelpers::addAll);

        return resHelpers;
    }

    public Set<Address> getWorkingLocations(String helperEmail) {
        Helper helperByEmail = this.findHelperByEmail(helperEmail);
        return helperByEmail.getAddressToWork();
    }

    @Transactional
    public void addHelpersJob(String email, List<String> jobNames) {
        Helper helper = findHelperByEmail(email);

        if (helper.getJobs() == null) {
            helper.setJobs(new HashSet<>());
        }

        for (String jobName : jobNames) {
            Optional<Job> job = jobRepository.findJobByName(jobName);
            if (job.isPresent()) {
                helper.getJobs().add(job.get());
            } else {
                helper.getJobs().add(new Job(jobName));
            }
        }
    }

    public List<String> getHelpersJob(String email) {
        Helper helper = findHelperByEmail(email);
        Set<Job> jobs = helper.getJobs();
        if (jobs != null) {
            return jobs.stream().map(Job::getName).toList();
        }
        return new ArrayList<>();
    }

    @Transactional
    public void addOfferingEmployer(String helperEmail, String employerEmail) {
        Employer employer = employerService.findEmployerByEmail(employerEmail);
        Helper helper = findHelperByEmail(helperEmail);
        if (helper.getOfferingEmployers() == null) {
            helper.setOfferingEmployers(new HashSet<>());
        }
        helper.getOfferingEmployers().add(employer);
    }

    public Set<Helper> getHelpersByLocationAndJobs(Address address, String jobName) {
        Optional<Job> job = jobRepository.findJobByName(jobName);
        if (job.isPresent()) {
            Optional<List<Helper>> optionalHelpers = helperRepository.findHelpersByAddressToWorkAndJob(address, job.get());
            if (optionalHelpers.isPresent())
                return new HashSet<>(optionalHelpers.get());
        } else {
            Job newJob = new Job(jobName);
            jobRepository.save(newJob);
//                jobs.add(newJob);
        }

        return new HashSet<>();
    }
}
