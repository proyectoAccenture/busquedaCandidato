package com.candidateSearch.searching.utility;

import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import com.candidateSearch.searching.repository.IStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
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
    private IJobProfileRepository jobProfileRepository;

    @Autowired
    private IRoleRepository vacancyCompanyRepository;

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

        String card = String.format("%010d", 1000000000L + random.nextInt(1000000000));
        String phone = String.format("%010d", 3000000000L + random.nextInt(1000000000));

        CandidateEntity candidate = new CandidateEntity(null, "name", "lastname", card, phone, "city", "email" + random.nextInt(1000) + "@gmail.com", LocalDate.of(1990, 5, 20), "source", "skills", "5 years", "work experience", "seniority", 1000000L, 1, LocalDate.of(2025, 1, 20), null, null, null, Status.ACTIVE, origin, jobProfile, new ArrayList<>());
        return candidateRepository.save(candidate);
    }

    public RoleEntity vacancyMethod(JobProfileEntity jobProfile, OriginEntity origin) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder roleNameBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            roleNameBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }

        String roleName = roleNameBuilder.toString();

        RoleEntity vacancy = new RoleEntity(null, roleName, "description", "contract", 1000000L, 1, "seniority", "skills", "experience", "assignment time", Status.ACTIVE, jobProfile, origin, null );
        return vacancyCompanyRepository.save(vacancy);
    }

    public StateEntity stateMethod() {
        StateEntity state = new StateEntity(null, "state");
        return stateRepository.save(state);
    }

    public PostulationEntity postulationMethod(CandidateEntity candidate, RoleEntity role) {
        PostulationEntity postulation = new PostulationEntity(null, LocalDate.of(2025, 1, 20), Status.ACTIVE, candidate, role, null);
        return postulationRepository.save(postulation);
    }

    public ProcessEntity processMethod(PostulationEntity postulation) {
        ProcessEntity process = new ProcessEntity(null, "description", LocalDate.now().plusDays(new Random().nextInt(10)), Status.ACTIVE, postulation, new ArrayList<>());
        return processRepository.save(process);
    }

    public CandidateStateEntity candidateStateMethod(ProcessEntity process, StateEntity state) {
        CandidateStateEntity candidateState = new CandidateStateEntity(null, "description", LocalDate.of(2025, 1, 20), true, Status.ACTIVE, state, process);
        CandidateStateEntity savedCandidateState = candidateStateRepository.save(candidateState);
        process.getCandidateState().add(savedCandidateState);
        processRepository.save(process);
        return savedCandidateState;
    }
}
