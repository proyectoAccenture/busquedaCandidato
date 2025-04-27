package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.CandidateStateRequestDto;
import com.candidateSearch.searching.dto.response.CandidateStateResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.CompanyVacancyEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
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
public class CandidateStateControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TestEntityFactory entityFactory;

    @Autowired
    private ICandidateStateRepository candidateStateRepository;

    @Test
    @DirtiesContext
    void get_candidate_state_by_id_should_return_200() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);
        RoleEntity role = entityFactory.roleMethod(vacancy);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);
        ProcessEntity process = entityFactory.processMethod(postulation);
        StateEntity state = entityFactory.stateMethod();
        CandidateStateEntity candidateState = entityFactory.candidateStateMethod(process, state);

        ResponseEntity<CandidateStateResponseDto> response = restTemplate.exchange(
                "/api/candidate_state/" + candidateState.getId(),
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
    void get_all_candidate_state_should_return_200() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);
        RoleEntity role = entityFactory.roleMethod(vacancy);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);
        ProcessEntity process = entityFactory.processMethod(postulation);
        StateEntity state = entityFactory.stateMethod();
        CandidateStateEntity candidateState1 = entityFactory.candidateStateMethod(process, state);
        CandidateStateEntity candidateState2 = entityFactory.candidateStateMethod(process, state);

        ResponseEntity<List<CandidateStateResponseDto>> response = restTemplate.exchange(
                "/api/candidate_state/",
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
    void create_candidate_state_should_return_201() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);
        RoleEntity role = entityFactory.roleMethod(vacancy);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);
        ProcessEntity process = entityFactory.processMethod(postulation);
        StateEntity state = entityFactory.stateMethod();

        CandidateStateRequestDto candidateStateRequestDto = new CandidateStateRequestDto();
        candidateStateRequestDto.setProcessId(process.getId());
        candidateStateRequestDto.setStateId(state.getId());
        candidateStateRequestDto.setStatus(true);
        candidateStateRequestDto.setDescription("Description");
        candidateStateRequestDto.setAssignedDate(LocalDate.now());

        ResponseEntity<CandidateStateResponseDto> response = restTemplate.exchange(
                "/api/candidate_state/",
                HttpMethod.POST,
                new HttpEntity<>(candidateStateRequestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @DirtiesContext
    void update_candidate_state_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);
        RoleEntity role = entityFactory.roleMethod(vacancy);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);
        ProcessEntity process = entityFactory.processMethod(postulation);
        StateEntity state = entityFactory.stateMethod();
        CandidateStateEntity candidateState1 = entityFactory.candidateStateMethod(process, state);

        CandidateStateRequestDto requestDto = new CandidateStateRequestDto();
        requestDto.setProcessId(process.getId());
        requestDto.setStateId(state.getId());
        requestDto.setStatus(true);
        requestDto.setDescription("description");
        requestDto.setAssignedDate(LocalDate.now());

        ResponseEntity<CandidateStateResponseDto> response = restTemplate.exchange(
                "/api/candidate_state/" + candidateState1.getId(),
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
    void delete_candidate_state_should_return_204() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CandidateEntity candidate = entityFactory.candidateMethod(jobProfile, origin);
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);
        RoleEntity role = entityFactory.roleMethod(vacancy);
        PostulationEntity postulation = entityFactory.postulationMethod(candidate, role);
        ProcessEntity process = entityFactory.processMethod(postulation);
        StateEntity state = entityFactory.stateMethod();
        CandidateStateEntity candidateState1 = entityFactory.candidateStateMethod(process, state);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/candidate_state/" + candidateState1.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(candidateStateRepository.existsById(postulation.getId()));
    }
}
