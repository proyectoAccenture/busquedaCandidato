package com.busquedaCandidato.candidato.utility;

import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

@Component
public class TestEntityFactory {
    @Autowired
    private ICandidateRepository candidateRepository;

    @Autowired
    private IOriginRepository originRepository;

    @Autowired
    private IRoleRepository roleIDRepository;

    @Autowired
    private IJobProfileRepository jobProfileRepository;

    @Autowired
    private ICompanyVacancyRepository vacancyCompanyRepository;

    @Autowired
    private IPostulationRepository postulationRepository;

    @Autowired
    private IStateRepository stateRepository;

    @Autowired
    private IProcessRepository processRepository;

    @Autowired
    private ICandidateStateRepository candidateStateRepository;

    public JobProfileEntity jobProfileMethod() {
        JobProfileEntity jobProfile = new JobProfileEntity(null, "Dev Backend", new ArrayList<>(), new ArrayList<>());
        return jobProfileRepository.save(jobProfile);
    }

    public OriginEntity originMethod() {
        OriginEntity origin = new OriginEntity(null, "origin", new ArrayList<>(), new ArrayList<>());
        return originRepository.save(origin);
    }

    public CandidateEntity candidateMethod(JobProfileEntity jobProfile, OriginEntity origin) {
        Random random = new Random();

        long card = 1000000000L + random.nextInt(1000000000);
        long phone = 3000000000L + random.nextInt(1000000000);

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", card, phone, "city", "email" + random.nextInt(1000) + "@gmail.com", LocalDate.of(1990, 5, 20), "source", "skills", "5 years", "work experience", "seniority", 1000000L, 1, LocalDate.of(2025, 1, 20), null, null, null, origin, jobProfile, new ArrayList<>());
        return candidateRepository.save(candidate);
    }

    public CompanyVacancyEntity vacancyMethod(JobProfileEntity jobProfile, OriginEntity origin) {
        CompanyVacancyEntity vacancy = new CompanyVacancyEntity(null, "description", "contract", 1000000L, 1, "seniority", "skills", "experience", "assignment time", jobProfile, origin, null );
        return vacancyCompanyRepository.save(vacancy);
    }

    public RoleEntity roleMethod(CompanyVacancyEntity vacancy) {
        RoleEntity role = new RoleEntity(null, "12345A", "description", vacancy,  new ArrayList<>());
        return roleIDRepository.save(role);
    }

    public StateEntity stateMethod() {
        StateEntity state = new StateEntity(null, "state");
        return stateRepository.save(state);
    }

    public PostulationEntity postulationMethod(CandidateEntity candidate, RoleEntity role) {
        PostulationEntity postulation = new PostulationEntity(null, LocalDate.of(2025, 1, 20), true, candidate, role, null);
        return postulationRepository.save(postulation);
    }

    public ProcessEntity processMethod(PostulationEntity postulation) {
        ProcessEntity process = new ProcessEntity(null, "description", LocalDate.now().plusDays(new Random().nextInt(10)), postulation, new ArrayList<>());
        return processRepository.save(process);
    }

    public CandidateStateEntity candidateStateMethod(ProcessEntity process, StateEntity state) {
        CandidateStateEntity candidateState = new CandidateStateEntity(null, "description", true, LocalDate.of(2025, 1, 20), state, process);
        CandidateStateEntity savedCandidateState = candidateStateRepository.save(candidateState);
        process.getCandidateState().add(savedCandidateState);
        processRepository.save(process);
        return savedCandidateState;
    }
}
