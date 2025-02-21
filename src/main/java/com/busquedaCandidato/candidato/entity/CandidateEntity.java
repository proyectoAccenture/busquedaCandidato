package com.busquedaCandidato.candidato.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Name", nullable = false, length = 50)
    private String name;
    @Column(name = "LastName", nullable = false, length = 50)
    private String lastName;
    @Column(name = "DocumentId", nullable = false, length = 50)
    private Long documentId;
    @Column(name = "DateOfBirth", nullable = false, length = 50)
    private String dateOfBirth;
    @Column(name = "Email", nullable = false, length = 50)
    private String email;
    @Column(name = "City", nullable = false, length = 50)
    private String city;

    public CandidateEntity(int id, String name, String lastName, Long documentId, String dateOfBirth, String email,
            String city) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.documentId = documentId;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.city = city;
    }


    public CandidateEntity(){};

    
    // get and set methods
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
