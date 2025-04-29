package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.PostulationRequestDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.utility.TestEntityFactory;
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
public class PostulationControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TestEntityFactory entityFactory;

    @Autowired
    private IPostulationRepository postulationRepository;

    @Test
    @DirtiesContext
    void get_postulation_by_id_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        RoleEntity role = entityFactory.vacancyMethod(jobProfile, origin);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);

        ResponseEntity<PostulationEntity> response = restTemplate.exchange(
                "/api/postulation/" + postulation.getId(),
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

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate1 = entityFactory.candidateMethod(jobProfile, origin);
        CandidateEntity candidate2 = entityFactory.candidateMethod(jobProfile, origin);
        RoleEntity role1 = entityFactory.vacancyMethod(jobProfile, origin);
        RoleEntity role2 = entityFactory.vacancyMethod(jobProfile, origin);
        PostulationEntity postulation1 = entityFactory.postulationMethod(candidate1, role1);
        PostulationEntity postulation2 = entityFactory.postulationMethod(candidate2, role2);


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

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        RoleEntity role = entityFactory.vacancyMethod(jobProfile, origin);

        PostulationRequestDto requestDto = new PostulationRequestDto();
        requestDto.setDatePresentation(LocalDate.now());
        requestDto.setRoleId(role.getId());
        requestDto.setCandidateId(candidate.getId());
        requestDto.setStatus(true);

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

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        RoleEntity role = entityFactory.vacancyMethod(jobProfile, origin);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);
        postulation.setStatus(false);
        postulationRepository.save(postulation);

        PostulationRequestDto updateDto = new PostulationRequestDto();
        updateDto.setDatePresentation(LocalDate.now().plusDays(1));
        updateDto.setRoleId(role.getId());
        updateDto.setCandidateId(candidate.getId());
        updateDto.setStatus(true);

        ResponseEntity<PostulationResponseDto> response = restTemplate.exchange(
                "/api/postulation/" + postulation.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateDto.getDatePresentation(), response.getBody().getDatePresentation());
    }

    @Test
    @DirtiesContext
    void delete_postulation_should_return_204() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        RoleEntity role = entityFactory.vacancyMethod(jobProfile, origin);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/postulation/" + postulation.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(postulationRepository.existsById(postulation.getId()));
    }
}

