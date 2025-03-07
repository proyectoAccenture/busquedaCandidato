package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.PostulationRequestDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.repository.*;
import com.busquedaCandidato.candidato.service.PostulationService;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Usa H2
public class PostulationControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IOriginRepository originRepository;

    @Autowired
    private IRoleIDRepository roleIDRepository;

    @Autowired
    private IJobProfileRepository jobProfileRepository;

    @Autowired
    private ICandidateRepository candidateRepository;

    @Autowired
    private IVacancyCompanyRepository vacancyCompanyRepository;

    @Autowired
    private IPostulationRepository postulationRepository;

    @Autowired
    private PostulationService postulationService;

    @Test
    @DirtiesContext
    void get_postulation_by_id_should_return_200() {

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity savedVacancy = vacancyCompanyRepository.save(vacancyCompany);

        CandidateEntity candidate = new CandidateEntity(null, "Juan", "Perez", 12345678L, LocalDate.of(1990, 5, 20), 3001234567L, "Bogotá", "juan.perez@example.com");
        CandidateEntity savedCandidate = candidateRepository.save(candidate);


        PostulationEntity postulation = new PostulationEntity(null, LocalDate.now(), "3.500.000", savedVacancy, savedCandidate);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        ResponseEntity<PostulationEntity> response = restTemplate.exchange(
                "/postulation/" + savedPostulation.getId(),
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
    void get_all_postulations_should_return_200() {

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity savedVacancy = vacancyCompanyRepository.save(vacancyCompany);

        CandidateEntity candidate = new CandidateEntity(null, "Juan", "Perez", 12345678L, LocalDate.of(1990, 5, 20), 3001234567L, "Bogotá", "juan.perez@example.com");
        CandidateEntity savedCandidate = candidateRepository.save(candidate);


        postulationRepository.save(new PostulationEntity(null, LocalDate.now(), "3.500.000", savedVacancy, savedCandidate));
        postulationRepository.save(new PostulationEntity(null, LocalDate.now(), "4.000.000", savedVacancy, savedCandidate));

        ResponseEntity<List<PostulationEntity>> response = restTemplate.exchange(
                "/postulation/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DirtiesContext
    void create_postulation_should_return_201() {

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity savedVacancy = vacancyCompanyRepository.save(vacancyCompany);

        CandidateEntity candidate = new CandidateEntity(null, "Juan", "Perez", 12345678L, LocalDate.of(1990, 5, 10), 9876543210L, "Bogotá", "juan.perez@example.com");
        CandidateEntity candidateSave = candidateRepository.save(candidate);


        PostulationRequestDto requestDto = new PostulationRequestDto();
        requestDto.setDatePresentation(LocalDate.now());
        requestDto.setSalaryAspiration("3.500.000");
        requestDto.setVacancyCompanyId(savedVacancy.getId());
        requestDto.setCandidateId(candidateSave.getId());

        ResponseEntity<PostulationResponseDto> response = restTemplate.exchange(
                "/postulation/",
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
    void update_postulation_should_return_200() {

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity savedVacancy = vacancyCompanyRepository.save(vacancyCompany);

        CandidateEntity candidate = candidateRepository.save(new CandidateEntity(null, "Juan", "Perez", 12345678L, LocalDate.of(1990, 5, 10), 9876543210L, "Bogotá", "juan.perez@example.com"));

        PostulationEntity postulation = postulationRepository.save(new PostulationEntity(null, LocalDate.now(), "3.200.000", savedVacancy, candidate));

        PostulationRequestDto updateRequest = new PostulationRequestDto();
        updateRequest.setDatePresentation(LocalDate.now());
        updateRequest.setSalaryAspiration("4.000.000");
        updateRequest.setVacancyCompanyId(savedVacancy.getId());
        updateRequest.setCandidateId(candidate.getId());

        ResponseEntity<PostulationResponseDto> response = restTemplate.exchange(
                "/postulation/" + postulation.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DirtiesContext
    void delete_postulation_should_return_204() {

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity savedVacancy = vacancyCompanyRepository.save(vacancyCompany);

        CandidateEntity candidate = candidateRepository.save(new CandidateEntity(null, "Juan", "Perez", 12345678L, LocalDate.of(1990, 5, 10), 9876543210L, "Bogotá", "juan.perez@example.com"));

        PostulationEntity postulation = postulationRepository.save(new PostulationEntity(null, LocalDate.now(), "3.200.000", savedVacancy, candidate));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/postulation/" + postulation.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(postulationRepository.existsById(postulation.getId()));
    }

}
