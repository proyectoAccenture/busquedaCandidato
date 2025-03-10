package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.VacancyCompanyRequestDto;
import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.dto.response.VacancyCompanyResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.repository.*;
import com.busquedaCandidato.candidato.service.VacancyCompanyService;
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
public class VacancyCompanyControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IVacancyCompanyRepository vacancyCompanyRepository;

    @Autowired
    private IOriginRepository originRepository;

    @Autowired
    private IRoleIDRepository roleIDRepository;

    @Autowired
    private IJobProfileRepository jobProfileRepository;

    @Autowired
    private VacancyCompanyService vacancyCompanyService;

    @Test
    @DirtiesContext
    void get_vacancy_by_id_should_return_200(){

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = vacancyCompanyRepository.save(
                new VacancyCompanyEntity(null, "Temporal", "2.500.000", "3 años", "Mid", "Python, AWS", "Desarrollador backend", LocalDate.now(), "LinkedIn", roleSave, jobProfileSave, originSave)
        );
        ResponseEntity<StateResponseDto> response = restTemplate.exchange(
                "/api/vacancy_company/" + vacancyCompany.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Verificar respuestas
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @DirtiesContext
    void get_all_vacancies_should_return_200() {

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        vacancyCompanyRepository.save(new VacancyCompanyEntity(null, "Temporal", "2.500.000", "3 años", "Mid", "Python, AWS", "Desarrollador backend", LocalDate.now(), "LinkedIn", roleSave, jobProfileSave, originSave));
        vacancyCompanyRepository.save(new VacancyCompanyEntity(null, "Indefinido", "4.500.000", "7 años", "Senior", "Java, Spring", "Desarrollador Fullstack", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave));

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

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyRequestDto requestDto = new VacancyCompanyRequestDto();
        requestDto.setContract("Indefinido");
        requestDto.setSalary("3.200.000");
        requestDto.setExperience("5 años");
        requestDto.setLevel("Senior");
        requestDto.setSkills("Java, AWS, Angular");
        requestDto.setDescription("Puesto para desarrollador backend");
        requestDto.setDatePublication(LocalDate.of(2025, 3, 1));
        requestDto.setSource("Workday");
        requestDto.setRole(roleSave.getId());
        requestDto.setJobProfile(jobProfileSave.getId());
        requestDto.setOrigin(originSave.getId());

        ResponseEntity<VacancyCompanyResponseDto> response = restTemplate.exchange(
                "/api/vacancy_company/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("3.200.000", response.getBody().getSalary());
    }

    @Test
    @DirtiesContext
    void update_vacancy_should_return_200() {
        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = vacancyCompanyRepository.save(
                new VacancyCompanyEntity(null, "Temporal", "2.500.000", "3 años", "Mid", "Python, AWS", "Desarrollador backend", LocalDate.now(), "LinkedIn", roleSave, jobProfileSave, originSave)
        );

        VacancyCompanyRequestDto updateRequest = new VacancyCompanyRequestDto();
        updateRequest.setContract("Indefinido");
        updateRequest.setSalary("4.500.000");
        updateRequest.setExperience("7 años");
        updateRequest.setLevel("Senior");
        updateRequest.setSkills("Java, Spring");
        updateRequest.setDescription("Desarrollador Fullstack");
        updateRequest.setDatePublication(LocalDate.now());
        updateRequest.setSource("Indeed");
        updateRequest.setRole(1L);
        updateRequest.setJobProfile(1L);
        updateRequest.setOrigin(1L);

        ResponseEntity<VacancyCompanyResponseDto> response = restTemplate.exchange(
                "/api/vacancy_company/" + vacancyCompany.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("4.500.000", response.getBody().getSalary());
    }

    @Test
    @DirtiesContext
    void delete_vacancy_should_return_204() {
        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = vacancyCompanyRepository.save(
                new VacancyCompanyEntity(null, "Temporal", "2.500.000", "3 años", "Mid", "Python, AWS", "Desarrollador backend", LocalDate.now(), "LinkedIn", roleSave, jobProfileSave, originSave)
        );

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/vacancy_company/" + vacancyCompany.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(vacancyCompanyRepository.existsById(vacancyCompany.getId()));
    }


}