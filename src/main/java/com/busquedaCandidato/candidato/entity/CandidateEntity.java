package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.boot.jaxb.Origin;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Candidato")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private Long IDCard;

    @Column(nullable = false)
    private Date birthdate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String city;

    @OneToOne
    @JoinColumn(name = "origin_id")
    private Origin origin;
}
