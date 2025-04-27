package com.busquedaCandidato.candidato.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "process")
public class ProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate assignmentDate;

    @OneToOne
    @JoinColumn(name = "postulation_id", nullable = false)
    @JsonBackReference
    private PostulationEntity postulation;

    @JsonManagedReference
    @OneToMany(mappedBy = "process",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<CandidateStateEntity> candidateState = new ArrayList<>();
}
