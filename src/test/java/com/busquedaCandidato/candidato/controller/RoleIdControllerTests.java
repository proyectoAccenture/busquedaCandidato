package com.busquedaCandidato.candidato.controller;


import com.busquedaCandidato.candidato.dto.request.RoleIDRequestDto;
import com.busquedaCandidato.candidato.dto.response.RoleIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import com.busquedaCandidato.candidato.service.RoleIDService;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Usa H2
public class RoleIdControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IRoleIDRepository roleIDRepository;

    @Autowired
    private RoleIDService roleIDService;

    @Test
    @DirtiesContext
    void get_role_by_id_should_return_200(){
        RoleIDEntity roleIDEntity = new RoleIDEntity(null, "Nuevo");
        RoleIDEntity saveEntity = roleIDRepository.save(roleIDEntity);

        ResponseEntity<RoleIDResponseDto> response = restTemplate.exchange(
                "/api/roleid/" + saveEntity.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nuevo", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void get_all_role_should_return_200() {
        roleIDRepository.save(new RoleIDEntity(null, "Rol1"));
        roleIDRepository.save(new RoleIDEntity(null, "Rol2"));

        ResponseEntity<List<RoleIDResponseDto>> response = restTemplate.exchange(
                "/api/roleid/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        List<String> roleNames = response.getBody().stream().map(RoleIDResponseDto::getName).toList();
        assertTrue(roleNames.contains("Rol1"));
        assertTrue(roleNames.contains("Rol2"));
    }

    @Test
    @DirtiesContext
    void create_role_should_return_201() {
        RoleIDRequestDto requestDto = new RoleIDRequestDto();
        requestDto.setName("V");
        ResponseEntity<RoleIDResponseDto> response = restTemplate.exchange(
                "/api/roleid/",
                HttpMethod.POST,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("V", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void update_role_should_return_200() {

        RoleIDEntity roleIDEntity = roleIDRepository.save(new RoleIDEntity(null, "En 2"));

        RoleIDRequestDto updateRequest = new RoleIDRequestDto();
        updateRequest.setName("En 3");

        ResponseEntity<RoleIDResponseDto> response = restTemplate.exchange(
                "/api/roleid/" + roleIDEntity.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("En 3", response.getBody().getName());
    }

    @Test
    @DirtiesContext
    void delete_role_should_return_204() {        // Guardar estado inicial

        RoleIDEntity roleIDEntity = roleIDRepository.save(new RoleIDEntity(null, "No id"));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/roleid/" + roleIDEntity.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(roleIDRepository.existsById(roleIDEntity.getId()));
    }
}