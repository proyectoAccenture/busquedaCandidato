package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import com.busquedaCandidato.candidato.utility.TestEntityFactory;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ProcessControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TestEntityFactory entityFactory;

    @Autowired
    IProcessRepository processRepository;

    @Test
    @DirtiesContext
    void get_process_by_id_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, vacancy);
        ProcessEntity process = entityFactory.processMethod(postulation);

        ResponseEntity<ProcessEntity> response = restTemplate.exchange(
                "/api/process/" + process.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @DirtiesContext
    void get_all_processes_should_return_200() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);
        CandidateEntity candidate2 = entityFactory.candidateMethod(jobProfile, origin);
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);
        PostulationEntity postulation1 = entityFactory.postulationMethod(candidate1, vacancy);
        PostulationEntity postulation2 = entityFactory.postulationMethod(candidate2, vacancy);
        ProcessEntity process1 = entityFactory.processMethod(postulation1);
        ProcessEntity process2 = entityFactory.processMethod(postulation2);

        ResponseEntity<List<ProcessEntity>> response = restTemplate.exchange(
                "/api/process/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DirtiesContext
    void create_process_should_return_201() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);
        PostulationEntity postulation1 = entityFactory.postulationMethod(candidate1, vacancy);

        ProcessRequestDto requestDto = new ProcessRequestDto();
        requestDto.setDescription("description");
        requestDto.setAssignedDate(LocalDate.now());
        requestDto.setPostulationId(postulation1.getId());

        ResponseEntity<ProcessResponseDto> response = restTemplate.exchange(
                "/api/process/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @DirtiesContext
    void update_process_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);
        PostulationEntity postulation1 = entityFactory.postulationMethod(candidate1, vacancy);
        ProcessEntity process1 = entityFactory.processMethod(postulation1);

        ProcessRequestDto requestDto = new ProcessRequestDto();
        requestDto.setDescription("description");
        requestDto.setAssignedDate(LocalDate.now());
        requestDto.setPostulationId(postulation1.getId());

        ResponseEntity<ProcessResponseDto> response = restTemplate.exchange(
                "/api/process/" + process1.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("description", response.getBody().getDescription());
    }


    @Test
    @DirtiesContext
    void delete_process_should_return_204() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);
        PostulationEntity postulation1 = entityFactory.postulationMethod(candidate1, vacancy);
        ProcessEntity process1 = entityFactory.processMethod(postulation1);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/process/" + process1.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(processRepository.existsById(postulation1.getId()));
    }
}
