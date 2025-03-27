package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.VacancyCompanyRequestDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.dto.response.VacancyCompanyResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
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
public class VacancyCompanyControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TestEntityFactory entityFactory;

    @Autowired
    private IVacancyCompanyRepository vacancyCompanyRepository;


    @Test
    @DirtiesContext
    void get_vacancy_by_id_should_return_200(){

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);

        ResponseEntity<StateResponseDto> response = restTemplate.exchange(
                "/api/vacancy_company/" + vacancy.getId(),
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
    void get_all_vacancies_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy1 = entityFactory.vacancyMethod(role, jobProfile, origin);
        VacancyCompanyEntity vacancy2 = entityFactory.vacancyMethod(role, jobProfile, origin);

        ResponseEntity<List<VacancyCompanyResponseDto>> response = restTemplate.exchange(
                "/api/vacancy_company/",
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
    void create_vacancy_should_return_201() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        RoleIDEntity role = entityFactory.roleMethod();

        VacancyCompanyRequestDto vacancyRequestDto = new VacancyCompanyRequestDto();
        vacancyRequestDto.setDescription("description");
        vacancyRequestDto.setContract("contract");
        vacancyRequestDto.setSalary(1000000L);
        vacancyRequestDto.setLevel(1);
        vacancyRequestDto.setSeniority("seniority");
        vacancyRequestDto.setSkills("skills");
        vacancyRequestDto.setAssignmentTime("assignment time");
        vacancyRequestDto.setRole(role.getId());
        vacancyRequestDto.setJobProfile(jobProfile.getId());
        vacancyRequestDto.setOrigin(origin.getId());

        ResponseEntity<VacancyCompanyResponseDto> response = restTemplate.exchange(
                "/api/vacancy_company/",
                HttpMethod.POST,
                new HttpEntity<>(vacancyRequestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DirtiesContext
    void update_vacancy_should_return_200() {

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);

        VacancyCompanyRequestDto updateRequest = new VacancyCompanyRequestDto();
        updateRequest.setDescription("Description");
        updateRequest.setContract("contract");
        updateRequest.setSalary(2500000L);
        updateRequest.setLevel(2);
        updateRequest.setSeniority("seniority");
        updateRequest.setSkills("Skills more");
        updateRequest.setAssignmentTime("Assignment day");
        updateRequest.setRole(1L);
        updateRequest.setJobProfile(1L);
        updateRequest.setOrigin(1L);

        ResponseEntity<VacancyCompanyResponseDto> response = restTemplate.exchange(
                "/api/vacancy_company/" + vacancy.getId(),
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
    void delete_vacancy_should_return_204() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        RoleIDEntity role = entityFactory.roleMethod();
        VacancyCompanyEntity vacancy = entityFactory.vacancyMethod(role, jobProfile, origin);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/vacancy_company/" + vacancy.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(vacancyCompanyRepository.existsById(vacancy.getId()));
    }
}