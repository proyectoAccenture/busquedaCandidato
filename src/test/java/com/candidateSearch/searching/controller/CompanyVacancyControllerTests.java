package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.CompanyVacancyRequestDto;
import com.candidateSearch.searching.dto.response.StateResponseDto;
import com.candidateSearch.searching.dto.response.CompanyVacancyResponseDto;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.CompanyVacancyEntity;
import com.candidateSearch.searching.repository.ICompanyVacancyRepository;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class CompanyVacancyControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TestEntityFactory entityFactory;

    @Autowired
    private ICompanyVacancyRepository vacancyCompanyRepository;


    @Test
    @DirtiesContext
    void get_vacancy_by_id_should_return_200(){

        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);

        ResponseEntity<StateResponseDto> response = restTemplate.exchange(
                "/api/company_vacancy/" + vacancy.getId(),
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
        CompanyVacancyEntity vacancy1 = entityFactory.vacancyMethod(jobProfile, origin);
        CompanyVacancyEntity vacancy2 = entityFactory.vacancyMethod(jobProfile, origin);

        ResponseEntity<List<CompanyVacancyResponseDto>> response = restTemplate.exchange(
                "/api/company_vacancy/",
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

        CompanyVacancyRequestDto vacancyRequestDto = new CompanyVacancyRequestDto();
        vacancyRequestDto.setDescription("description");
        vacancyRequestDto.setContract("contract");
        vacancyRequestDto.setSalary(1000000L);
        vacancyRequestDto.setLevel(1);
        vacancyRequestDto.setSeniority("seniority");
        vacancyRequestDto.setSkills("skills");
        vacancyRequestDto.setExperience("experience");
        vacancyRequestDto.setAssignmentTime("assignment time");
        vacancyRequestDto.setJobProfile(jobProfile.getId());
        vacancyRequestDto.setOrigin(origin.getId());

        ResponseEntity<CompanyVacancyResponseDto> response = restTemplate.exchange(
                "/api/company_vacancy/",
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
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);

        CompanyVacancyRequestDto updateRequest = new CompanyVacancyRequestDto();
        updateRequest.setDescription("Description");
        updateRequest.setContract("contract");
        updateRequest.setSalary(2500000L);
        updateRequest.setLevel(2);
        updateRequest.setSeniority("seniority");
        updateRequest.setSkills("Skills more");
        updateRequest.setExperience("experience");
        updateRequest.setAssignmentTime("Assignment day");
        updateRequest.setJobProfile(1L);
        updateRequest.setOrigin(1L);

        ResponseEntity<CompanyVacancyResponseDto> response = restTemplate.exchange(
                "/api/company_vacancy/" + vacancy.getId(),
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
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/company_vacancy/" + vacancy.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(vacancyCompanyRepository.existsById(vacancy.getId()));
    }
}