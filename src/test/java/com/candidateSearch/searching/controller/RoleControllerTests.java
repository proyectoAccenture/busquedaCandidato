package com.candidateSearch.searching.controller;

import com.candidateSearch.searching.dto.request.RoleRequestDto;
import com.candidateSearch.searching.dto.response.RoleResponseDto;
import com.candidateSearch.searching.entity.CompanyVacancyEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.service.RoleService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class RoleControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IRoleRepository roleIDRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    TestEntityFactory entityFactory;


    @Test
    @DirtiesContext
    void get_role_by_id_should_return_200(){
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);

        RoleEntity roleEntity = new RoleEntity(null, "12345A", "Description", vacancy, null);
        RoleEntity saveEntity = roleIDRepository.save(roleEntity);

        ResponseEntity<RoleResponseDto> response = restTemplate.exchange(
                "/api/role_id/" + saveEntity.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345A", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void get_all_role_should_return_200() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CompanyVacancyEntity vacancy1 = entityFactory.vacancyMethod(jobProfile, origin);
        CompanyVacancyEntity vacancy2 = entityFactory.vacancyMethod(jobProfile, origin);


        roleIDRepository.save(new RoleEntity(null, "12345A", "Description", vacancy1, null));
        roleIDRepository.save(new RoleEntity(null, "12345E", "Description", vacancy2, null));

        ResponseEntity<List<RoleResponseDto>> response = restTemplate.exchange(
                "/api/role_id/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        List<String> roleNames = response.getBody().stream().map(RoleResponseDto::getName).toList();
        assertTrue(roleNames.contains("12345A"));
        assertTrue(roleNames.contains("12345E"));
    }

    @Test
    @DirtiesContext
    void create_role_should_return_201() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);

        RoleRequestDto requestDto = new RoleRequestDto();
        requestDto.setName("12345A");
        requestDto.setDescription("Description");
        requestDto.setVacancyCompanyId(vacancy.getId());

        ResponseEntity<RoleResponseDto> response = restTemplate.exchange(
                "/api/role_id/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345A", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void update_role_should_return_200() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);

        RoleEntity role = roleIDRepository.save(new RoleEntity(null, "12345A", "Description", vacancy, null));

        RoleRequestDto updateRequest = new RoleRequestDto();
        updateRequest.setName("123456A");
        updateRequest.setDescription("Description new");
        updateRequest.setVacancyCompanyId(vacancy.getId());

        ResponseEntity<RoleResponseDto> response = restTemplate.exchange(
                "/api/role_id/" + role.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123456A", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void delete_role_should_return_204() {
        OriginEntity origin = entityFactory.originMethod();
        JobProfileEntity jobProfile = entityFactory.jobProfileMethod();
        CompanyVacancyEntity vacancy = entityFactory.vacancyMethod(jobProfile, origin);

        RoleEntity role = roleIDRepository.save(new RoleEntity(null, "12345A", "Description", vacancy, null));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/role_id/" + role.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(roleIDRepository.existsById(role.getId()));
    }
}