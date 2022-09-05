package com.my.exhibitions.entities;

import com.my.exhibitions.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 21, message = "Username should be between 3 and 21 characters")
    private String username;

    @Column(nullable = false)
    @NotEmpty(message = "Password should not be empty")
    private String password;

    @Column(nullable = false)
    private Role role;

    @ManyToMany(mappedBy = "users")
    private List<Exhibition> exhibitions;
}