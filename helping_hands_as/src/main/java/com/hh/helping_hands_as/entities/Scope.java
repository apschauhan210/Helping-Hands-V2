package com.hh.helping_hands_as.entities;

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
public class Scope {

    @Id
    @SequenceGenerator(name = "scope_id_generator", sequenceName = "scope_id_generator", allocationSize = 1)
    @GeneratedValue(generator = "scope_id_generator")
    private int id;

    private String name;

    @ManyToMany(mappedBy = "scopes")
    private Set<Client> clients;

    public Scope(String name) {
        this.name = name;
    }
}
