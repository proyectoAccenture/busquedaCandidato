package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
<<<<<<< HEAD
@Table(name = "job_profile")
=======
@Table(name = "JobProfile")
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594
public class JobProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}

