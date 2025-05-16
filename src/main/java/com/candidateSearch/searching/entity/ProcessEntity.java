package com.candidateSearch.searching.entity;

import com.candidateSearch.searching.entity.utility.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    @OneToOne
    @JoinColumn(name = "postulation_id", nullable = false)
    @JsonBackReference
    private PostulationEntity postulation;

    @JsonManagedReference
    @OneToMany(mappedBy = "process")
    private List<CandidateStateEntity> candidateState = new ArrayList<>();
}
