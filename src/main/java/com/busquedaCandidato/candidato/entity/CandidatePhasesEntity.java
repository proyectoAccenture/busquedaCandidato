package com.busquedaCandidato.candidato.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
<<<<<<< HEAD:src/main/java/com/busquedaCandidato/candidato/entity/CandidatePhasesEntity.java
@Table(name = "candidate_phases")
public class CandidatePhasesEntity {
=======
@Table(name = "CandidateProcess")
public class CandidateProcessEntity {
>>>>>>> 6a17b9c9e6c656605a65ec6b0e672481d1841594:src/main/java/com/busquedaCandidato/candidato/entity/CandidateProcessEntity.java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private LocalDate assignedDate;


    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false)
    private PhaseEntity phase;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private StateEntity state;


}
