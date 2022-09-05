package com.my.exhibitions.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "halls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exhibition_sequence")
    @SequenceGenerator(name = "exhibition_sequence", sequenceName = "exhibition_sequence", allocationSize = 1)
    public long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 21, message = "Name should be between 3 and 21 characters")
    private String name;

    @Column(nullable = false)
    @Size(min = 3, message = "Description should be between 3 and 21 characters")
    private String description;

    @ManyToMany(mappedBy = "halls")
    private Set<Exhibition> exhibitions;
}