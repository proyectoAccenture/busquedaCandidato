package com.busquedaCandidato.candidato.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.repository.JobProfileRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/jobprofiles")
public class JobProfileController {

@Autowired
private JobProfileRepository jobProfileRepository;

@GetMapping
public List<JobProfileEntity> getAllJobProfiles(){ 
    return jobProfileRepository.findAll();
}
@PostMapping
public JobProfileEntity createJobProfile(@RequestBody JobProfileEntity jobProfileEntity) {
    return jobProfileRepository.save(jobProfileEntity);
}
@PutMapping("/{id}")
public ResponseEntity<JobProfileEntity> updateJobProfile(@PathVariable Long id, @RequestBody JobProfileEntity jobProfileEntity){
    if (jobProfileRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    jobProfileEntity.setId(id);
    JobProfileEntity updateJobProfile = jobProfileRepository.save(jobProfileEntity);
    return ResponseEntity.ok(updateJobProfile);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void>deleteJobProfile(@PathVariable Long id){
 if (!jobProfileRepository.existsById(id)) {
    return ResponseEntity.notFound().build();
 }
 jobProfileRepository.deleteById(id);
 return ResponseEntity.noContent().build();
}
}



