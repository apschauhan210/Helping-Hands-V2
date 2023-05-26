package com.hh.helping_hands_rs.entities;

import com.hh.helping_hands_rs.models.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Helper {

    @Id
    @SequenceGenerator(name = "helper_id_generator", sequenceName = "helper_id_generator", allocationSize = 10)
    @GeneratedValue(generator = "helper_id_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String mobileNumber;

    @ElementCollection
    private Set<Address> addressToWork;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "helper_employer",
            joinColumns = @JoinColumn(name = "helper_id"),
            inverseJoinColumns = @JoinColumn(name = "employer_id")
    )
    private Set<Employer> offeringEmployers;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "helper_job",
            joinColumns = @JoinColumn(name = "helper_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private Set<Job> jobs;

    public Helper(String email, String name, String mobileNumber, Set<Address> addressToWork, Set<Employer> offeringEmployers, Set<Job> jobs) {
        this.email = email;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.addressToWork = addressToWork;
        this.offeringEmployers = offeringEmployers;
        this.jobs = jobs;
    }
}
