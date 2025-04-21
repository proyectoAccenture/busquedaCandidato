package com.busquedaCandidato.candidato.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "postulation")
public class PostulationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datePresentation;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleIDEntity role;

    @OneToOne(mappedBy = "postulation")
    private ProcessEntity process;
    
}
