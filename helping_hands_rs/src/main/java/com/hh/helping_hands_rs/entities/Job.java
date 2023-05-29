package com.hh.helping_hands_rs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Job {

    @Id
    @SequenceGenerator(name = "job_id_generator", sequenceName = "job_id_generator", allocationSize = 1)
    @GeneratedValue(generator = "job_id_generator")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "jobs", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Helper> helpers;

    public Job(String name) {
        this.name = name;
    }
}
