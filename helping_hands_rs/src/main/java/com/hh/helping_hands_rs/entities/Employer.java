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
public class Employer {

    @Id
    @SequenceGenerator(name = "employer_id_generator", sequenceName = "employer_id_generator", allocationSize = 10)
    @GeneratedValue(generator = "employer_id_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String mobileNumber;

    @ManyToMany(mappedBy = "offeringEmployers", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Helper> requestedHelpers;

    public Employer(String email, String name, String mobileNumber, Set<Helper> requestedHelpers) {
        this.email = email;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.requestedHelpers = requestedHelpers;
    }
}
