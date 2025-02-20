package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Estado")
public class StateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

}
