package com.busquedaCandidato.candidato.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Vacant")
public class VacantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "AplicatioDate", nullable = false, length = 50)
    private String aplicationDate;
    @Column(name = "CurrentSalary", nullable = false, length = 50)
    private int currentSalary;
    @Column(name = "SalaryAspitarion", nullable = false, length = 50)
    private int salaryAspitarion;
    @Column(name = "Experience", nullable = false, length = 50)
    private String experience;
    @Column(name ="Level", nullable= false, length = 50)
    private String level;
    @Column(name ="Level", nullable= false, length = 50)
    private String skill;
    @Column(name ="Level", nullable= false, length = 50)
    private String contract;

    public VacantEntity(){}
    
    public VacantEntity(int id, String aplicationDate, int currentSalary, int salaryAspitarion, String experience,
            String level, String skill, String contract) {
        this.id = id;
        this.aplicationDate = aplicationDate;
        this.currentSalary = currentSalary;
        this.salaryAspitarion = salaryAspitarion;
        this.experience = experience;
        this.level = level;
        this.skill = skill;
        this.contract = contract;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAplicationDate() {
        return aplicationDate;
    }
    public void setAplicationDate(String aplicationDate) {
        this.aplicationDate = aplicationDate;
    }
    public int getCurrentSalary() {
        return currentSalary;
    }
    public void setCurrentSalary(int currentSalary) {
        this.currentSalary = currentSalary;
    }
    public int getSalaryAspitarion() {
        return salaryAspitarion;
    }
    public void setSalaryAspitarion(int salaryAspitarion) {
        this.salaryAspitarion = salaryAspitarion;
    }
    public String getExperience() {
        return experience;
    }
    public void setExperience(String experience) {
        this.experience = experience;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getSkill() {
        return skill;
    }
    public void setSkill(String skill) {
        this.skill = skill;
    }
    public String getContract() {
        return contract;
    }
    public void setContract(String contract) {
        this.contract = contract;
    }




}
