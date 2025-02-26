package com.busquedaCandidato.candidato.entity;

<<<<<<< Updated upstream
import java.sql.Date;
=======
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;
>>>>>>> Stashed changes

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name=("Process"))
public class ProcessEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
@Column(name = "Date", nullable = false, length = 50)
private Date date;
@Column(name = "Description", nullable = false, length = 50)
private String description;
@Column(name = "State", nullable = false, length = 50)
private String state;


}
