package com.candidateSearch.searching.entity;

import com.candidateSearch.searching.utility.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "candidate")
public class CandidateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String card;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String skills;

    @Column(nullable = false)
    private String yearsExperience;

    @Column(nullable = false)
    private String workExperience;

    @Column(nullable = false)
    private String seniority;

    @Column(nullable = false)
    private Long salaryAspiration;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private LocalDate datePresentation;

    @Lob
    @Column(name = "resume_pdf", columnDefinition = "LONGBLOB")
    private byte[] resumePdf;

    @Column(name = "resume_file_name")
    private String resumeFileName;

    @Column(name = "resume_content_type")
    private String resumeContentType;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "origin_id")
    private OriginEntity origin;

    @ManyToOne
    @JoinColumn(name = "job_profile_id")
    private JobProfileEntity jobProfile;

    @OneToMany(mappedBy = "candidate")
    private List<PostulationEntity> postulations = new ArrayList<>();
}