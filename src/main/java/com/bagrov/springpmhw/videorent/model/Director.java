package com.bagrov.springpmhw.videorent.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "directors_sequence", allocationSize = 1)
public class Director{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_generator")
    private int id;

    @Column(name = "directors_fio", nullable = false)
    private String directorsFIO;

    @Column(name = "position")
    private String position;

    @ManyToMany
    @JoinTable(name = "films_directors",
            joinColumns = @JoinColumn(name = "director_id"),
            foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
            inverseJoinColumns = @JoinColumn(name = "film_id"),
            inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS")
    )
    private List<Film> films;
}
