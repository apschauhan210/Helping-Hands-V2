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
public class User {

    @Id
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_generator", allocationSize = 1)
    @GeneratedValue(generator = "user_id_generator")
    private int id;

    @Column(unique = true, nullable = false)
    private String username;   // email

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;
    private String lastName;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = {
//                    @JoinColumn(name = "authority_id", referencedColumnName = "id"),
//                    @JoinColumn(name = "authority_name", referencedColumnName = "name")
//            }
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
