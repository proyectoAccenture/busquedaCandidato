package com.candidateSearch.searching.entity;

import com.candidateSearch.searching.entity.utility.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "candidate_state")
public class CandidateStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate assignedDate;

    @Column(nullable = false)
    private Boolean status;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status statusHistory;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "process_id", nullable = false)
    private ProcessEntity process;
}
