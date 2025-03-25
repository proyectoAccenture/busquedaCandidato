package com.busquedaCandidato.candidato.controller;

import com.busquedaCandidato.candidato.dto.request.CandidateStateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateStateResponseDto;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CandidatePhasesControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ICandidateStateRepository candidatePhasesRepository;

    @Autowired
    private IProcessRepository processRepository;

    @Autowired
    private IPhaseRepository phaseRepository;

    @Autowired
    private IStateRepository stateRepository;

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
    void get_candidate_phases_by_id_should_return_200() {
        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20));
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", 300000L, "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, 1000000L, LocalDate.of(2025, 5, 20), true, candidateSave, vacancySave, null);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        PhaseEntity phaseEntity = new PhaseEntity(null, "phase");
        PhaseEntity phaseSave = phaseRepository.save(phaseEntity);

        StateEntity stateEntity = new StateEntity(null, "state");
        StateEntity stateSave = stateRepository.save(stateEntity);

        ProcessEntity processEntity = new ProcessEntity(null, "description", LocalDate.of(2025, 1, 20), savedPostulation, new ArrayList<>());
        ProcessEntity processSave = processRepository.save(processEntity);

        CandidateStateEntity candidateStateEntity = new CandidateStateEntity(null, "description", true, LocalDate.of(2025, 1, 20), phaseSave, stateSave, processSave);
        CandidateStateEntity candidatePhasesSave = candidatePhasesRepository.save(candidateStateEntity);

        ResponseEntity<CandidateStateResponseDto> response = restTemplate.exchange(
                "/api/candidate_phases/" + candidatePhasesSave.getId(),
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
    void get_all_candidate_phases_should_return_200() {
        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20));
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        PhaseEntity phaseEntity = new PhaseEntity(null, "phase");
        PhaseEntity phaseSave = phaseRepository.save(phaseEntity);

        StateEntity stateEntity = new StateEntity(null, "state");
        StateEntity stateSave = stateRepository.save(stateEntity);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", 300000L, "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, 1000000L, LocalDate.of(2025, 5, 20), true, candidateSave, vacancySave, null);
        PostulationEntity postulationSave = postulationRepository.save(postulation);

        ProcessEntity processEntity = new ProcessEntity(null, "description", LocalDate.of(2025, 1, 20), postulationSave, new ArrayList<>());
        ProcessEntity processSave = processRepository.save(processEntity);

        candidatePhasesRepository.save(new CandidateStateEntity(null, "description", true, LocalDate.of(2025, 1, 20), phaseSave, stateSave, processSave));
        candidatePhasesRepository.save(new CandidateStateEntity(null, "description", true, LocalDate.of(2025, 1, 20), phaseSave, stateSave, processSave));


        ResponseEntity<List<CandidateStateResponseDto>> response = restTemplate.exchange(
                "/api/candidate_phases/",
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
    void create_candidate_phases_should_return_201() {

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20));
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", 300000L, "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, 1000000L, LocalDate.of(2025, 5, 20), true, candidateSave, vacancySave, null);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        PhaseEntity phaseEntity = new PhaseEntity(null, "phase");
        PhaseEntity phaseSave = phaseRepository.save(phaseEntity);

        StateEntity stateEntity = new StateEntity(null, "state");
        StateEntity stateSave = stateRepository.save(stateEntity);

        ProcessEntity processEntity = new ProcessEntity(null, "description", LocalDate.of(2025, 1, 20), savedPostulation, new ArrayList<>());
        ProcessEntity processSave = processRepository.save(processEntity);

        candidatePhasesRepository.save(new CandidateStateEntity(null, "description", true, LocalDate.of(2025, 1, 20), phaseSave, stateSave, processSave));

        CandidateStateRequestDto requestDto = new CandidateStateRequestDto();
        requestDto.setDescription("Description");
        requestDto.setStatus(true);
        requestDto.setAssignedDate(LocalDate.now());
        requestDto.setPhaseId(phaseSave.getId());
        requestDto.setStateId(stateSave.getId());
        requestDto.setProcessId(processSave.getId());

        ResponseEntity<CandidateStateResponseDto> response = restTemplate.exchange(
                "/api/candidate_phases/",
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
    void update_candidate_phases_should_return_200() {

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20));
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", 300000L, "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, 1000000L, LocalDate.of(2025, 5, 20), true, candidateSave, vacancySave, null);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        ProcessEntity processEntity = new ProcessEntity(null, "Old description", LocalDate.of(2025, 1, 20), savedPostulation, new ArrayList<>());
        ProcessEntity savedProcess = processRepository.save(processEntity);

        PhaseEntity phaseEntity = new PhaseEntity(null, "phase");
        PhaseEntity phaseSave = phaseRepository.save(phaseEntity);

        StateEntity stateEntity = new StateEntity(null, "state");
        StateEntity stateSave = stateRepository.save(stateEntity);

        CandidateStateEntity candidateStateEntity = candidatePhasesRepository.save(new CandidateStateEntity(null, "description", true, LocalDate.of(2025, 1, 20), phaseSave, stateSave, savedProcess));

        CandidateStateRequestDto requestDto = new CandidateStateRequestDto();
        requestDto.setDescription("Description new");
        requestDto.setStatus(true);
        requestDto.setAssignedDate(LocalDate.now());
        requestDto.setPhaseId(phaseSave.getId());
        requestDto.setStateId(stateSave.getId());
        requestDto.setProcessId(savedProcess.getId());

        ResponseEntity<CandidateStateResponseDto> response = restTemplate.exchange(
                "/api/candidate_phases/" + candidateStateEntity.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(requestDto),
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Description new", response.getBody().getDescription());
    }


    @Test
    @DirtiesContext
    void delete_candidate_phases_should_return_204() {
        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", 1000234567L, 3002004050L, "medellin", "santiago@gmail.com", LocalDate.of(1990, 5, 20), LocalDate.of(2025, 1, 20));
        CandidateEntity candidateSave = candidateRepository.save(candidate);

        OriginEntity origin = new OriginEntity(null, "interno");
        OriginEntity originSave = originRepository.save(origin);

        RoleIDEntity role = new RoleIDEntity(null, "12345A");
        RoleIDEntity roleSave = roleIDRepository.save(role);

        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend");
        JobProfileEntity jobProfileSave = jobProfileRepository.save(jobProfile);

        VacancyCompanyEntity vacancyCompany = new VacancyCompanyEntity(null, "Indefinido", 300000L, "5 años", "Mid", "Java, Spring", "Desarrollador Backend", LocalDate.now(), "Indeed", roleSave, jobProfileSave, originSave);
        VacancyCompanyEntity vacancySave = vacancyCompanyRepository.save(vacancyCompany);

        PostulationEntity postulation = new PostulationEntity(null, 1000000L, LocalDate.of(2025, 5, 20), true, candidateSave, vacancySave, null);
        PostulationEntity savedPostulation = postulationRepository.save(postulation);

        ProcessEntity processEntity = new ProcessEntity(null, "Old description", LocalDate.of(2025, 1, 20), savedPostulation, new ArrayList<>());
        ProcessEntity savedProcess = processRepository.save(processEntity);

        PhaseEntity phaseEntity = new PhaseEntity(null, "phase");
        PhaseEntity phaseSave = phaseRepository.save(phaseEntity);

        StateEntity stateEntity = new StateEntity(null, "state");
        StateEntity stateSave = stateRepository.save(stateEntity);

        CandidateStateEntity candidateStateEntity = candidatePhasesRepository.save(new CandidateStateEntity(null, "description", true, LocalDate.of(2025, 1, 20), phaseSave, stateSave, savedProcess));


        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/candidate_phases/" + candidateStateEntity.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(candidatePhasesRepository.existsById(postulation.getId()));
    }
}
