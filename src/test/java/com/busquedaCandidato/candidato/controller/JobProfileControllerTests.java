package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import com.busquedaCandidato.candidato.service.JobProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class JobProfileControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IJobProfileRepository jobProfileRepository;

    @Autowired
    private JobProfileService jobProfileService;

    @Test
    @DirtiesContext
    void get_job_profile_by_id_should_return_200() {
        JobProfileEntity jobProfileEntity = new JobProfileEntity(null, "Perfil 1");
        JobProfileEntity savedEntity = jobProfileRepository.save(jobProfileEntity);

        ResponseEntity<JobProfileResponseDto> response = restTemplate.exchange(
                "/api/jobprofile/" + savedEntity.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Perfil 1", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void get_all_job_profiles_should_return_200() {
        jobProfileRepository.save(new JobProfileEntity(null, "Perfil 2"));
        jobProfileRepository.save(new JobProfileEntity(null, "Perfil 3"));

        ResponseEntity<List<JobProfileResponseDto>> response = restTemplate.exchange(
                "/api/jobprofile/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        List<String> jobProfileNames = response.getBody().stream().map(JobProfileResponseDto::getName).toList();
        assertTrue(jobProfileNames.contains("Perfil 2"));
        assertTrue(jobProfileNames.contains("Perfil 3"));
    }

    @Test
    @DirtiesContext
    void create_job_profile_should_return_201() {
        JobProfileRequestDto requestDto = new JobProfileRequestDto();
        requestDto.setName("Nuevo");

        ResponseEntity<JobProfileResponseDto> response = restTemplate.exchange(
                "/api/jobprofile/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nuevo", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void update_job_profile_should_return_200() {
        JobProfileEntity jobProfileEntity = jobProfileRepository.save(new JobProfileEntity(null, "Perfil 3"));
        JobProfileRequestDto updateRequest = new JobProfileRequestDto();
        updateRequest.setName("Perfil 4");

        ResponseEntity<JobProfileResponseDto> response = restTemplate.exchange(
                "/api/jobprofile/" + jobProfileEntity.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Perfil 4", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void delete_job_profile_should_return_204() {
        JobProfileEntity jobProfileEntity = jobProfileRepository.save(new JobProfileEntity(null, "Perfil 1"));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/jobprofile/" + jobProfileEntity.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(jobProfileRepository.existsById(jobProfileEntity.getId()));
    }
}
