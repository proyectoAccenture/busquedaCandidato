package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
<<<<<<< HEAD
@Table(name = "state")
=======
@Table(name = "State")
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594
public class StateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
