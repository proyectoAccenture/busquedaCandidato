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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PostulationControllerTests {
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

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20) );
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, "1.000.000", LocalDate.of(1990, 5, 20), candidateSave, vacancySave);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        ResponseEntity<PostulationEntity> response = restTemplate.exchange(
                "/api/postulation/" + savedPostulation.getId(),
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

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20) );
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        postulationRepository.save(new PostulationEntity(null, "3.500.000",  LocalDate.of(2025, 5, 20), candidateSave, vacancySave));
        postulationRepository.save(new PostulationEntity(null, "4.000.000",  LocalDate.of(2025, 5, 20), candidateSave, vacancySave));

        ResponseEntity<List<PostulationEntity>> response = restTemplate.exchange(
                "/api/postulation/",
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

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20) );
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);


        PostulationRequestDto requestDto = new PostulationRequestDto();
        requestDto.setSalaryAspiration("3.500.000");
        requestDto.setDatePresentation(LocalDate.now());
        requestDto.setVacancyCompanyId(vacancySave.getId());
        requestDto.setCandidateId(candidateSave.getId());

        ResponseEntity<PostulationResponseDto> response = restTemplate.exchange(
                "/api/postulation/",
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

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20) );
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, "1.000.000", LocalDate.of(1990, 5, 20), candidateSave, vacancySave);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        PostulationRequestDto requestDto = new PostulationRequestDto();
        requestDto.setSalaryAspiration("3.500.000");
        requestDto.setDatePresentation(LocalDate.now());
        requestDto.setVacancyCompanyId(vacancySave.getId());
        requestDto.setCandidateId(candidateSave.getId());

        ResponseEntity<PostulationResponseDto> response = restTemplate.exchange(
                "/api/postulation/" + savedPostulation.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DirtiesContext
    void delete_postulation_should_return_204() {

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20) );
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", "4.000.000", "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, "1.000.000", LocalDate.of(1990, 5, 20), candidateSave, vacancySave);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/postulation/" + savedPostulation.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(postulationRepository.existsById(postulation.getId()));
    }
}

