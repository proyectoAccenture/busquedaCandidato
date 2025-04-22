package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class CandidateControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TestEntityFactory entityFactory;

    @Autowired
    ICandidateRepository candidateRepository;

    @Test
    @DirtiesContext
    void get_candidate_by_id_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);

        ResponseEntity<CandidateEntity> response = restTemplate.exchange(
                "/api/candidate/id/" + candidate.getId(),
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
    void get_all_candidates_should_return_200() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);
        CandidateEntity candidate2 = entityFactory.candidateMethod(jobProfile, origin);

        ResponseEntity<List<CandidateEntity>> response = restTemplate.exchange(
                "/api/candidate/",
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
    void create_candidate_should_return_201() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();

        CandidateRequestDto requestDto = new CandidateRequestDto();
        requestDto.setName("name");
        requestDto.setLastName("lastName");
        requestDto.setCard(1000000077L);
        requestDto.setPhone(3005003020L);
        requestDto.setCity("city");
        requestDto.setEmail("email@gmail.com");
        requestDto.setBirthdate(LocalDate.of(1990, 5, 20));
        requestDto.setSource("source");
        requestDto.setSkills("skills");
        requestDto.setYearsExperience("5 years");
        requestDto.setWorkExperience("work experience");
        requestDto.setSeniority("seniority");
        requestDto.setSalaryAspiration(1000000L);
        requestDto.setLevel(1);
        requestDto.setDatePresentation(LocalDate.now());
        requestDto.setOrigin(origin.getId());
        requestDto.setJobProfile(jobProfile.getId());

        ResponseEntity<CandidateResponseDto> response = restTemplate.exchange(
                "/api/candidate/",
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
    void update_candidate_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);

        CandidateRequestDto requestDto = new CandidateRequestDto();
        requestDto.setName("name");
        requestDto.setLastName("lastName");
        requestDto.setCard(1000000077L);
        requestDto.setPhone(3005003020L);
        requestDto.setCity("city");
        requestDto.setEmail("email@gmail.com");
        requestDto.setBirthdate(LocalDate.of(1990, 5, 20));
        requestDto.setSource("source");
        requestDto.setSkills("skills");
        requestDto.setYearsExperience("5 years");
        requestDto.setWorkExperience("work experience");
        requestDto.setSeniority("seniority");
        requestDto.setSalaryAspiration(1000000L);
        requestDto.setLevel(1);
        requestDto.setDatePresentation(LocalDate.now());
        requestDto.setOrigin(origin.getId());
        requestDto.setJobProfile(jobProfile.getId());

        ResponseEntity<CandidateResponseDto> response = restTemplate.exchange(
                "/api/candidate/" + candidate1.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("city", response.getBody().getCity());
    }


    @Test
    @DirtiesContext
    void delete_candidate_should_return_204() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/candidate/" + candidate1.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(candidateRepository.existsById(candidate1.getId()));
    }
}
