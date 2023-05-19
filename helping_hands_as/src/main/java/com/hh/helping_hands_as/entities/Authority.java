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
public class Authority {

    @Id
    @SequenceGenerator(name = "authority_id_generator", sequenceName = "authority_id_generator", allocationSize = 1)
    @GeneratedValue(generator = "authority_id_generator")
    private int id;

    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;

    public Authority(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Authority e = (Authority) obj;
        return this.name.equals(e.name);
    }
}
