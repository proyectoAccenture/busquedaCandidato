package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.exception.type.CandidateNoExistException;
import com.busquedaCandidato.candidato.exception.type.IdCardAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.PhoneAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.RoleIdNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateRequest;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    private final IVacancyCompanyRepository vacancyCompanyRepository;
    private final IPostulationRepository postulationRepository;
    private final IRoleIDRepository roleIDRepository;
    private final IMapperCandidateResponse mapperCandidateResponse;
    private final IMapperCandidateRequest mapperCandidateRequest;

    public List<CandidateResponseDto> getCandidateByRole(String roleName) {

        RoleIDEntity roleOptional = roleIDRepository.findByName(roleName)
                .orElseThrow(RoleIdNoExistException::new);

        Long roleId = roleOptional.getId();

        List<VacancyCompanyEntity> vacancies = vacancyCompanyRepository.findByRoleId(roleId);
        List<Long> vacancyIds = vacancies.stream()
                .map(VacancyCompanyEntity::getId)
                .toList();

        List<PostulationEntity> postulations = postulationRepository.findByVacancyCompanyIdIn(vacancyIds);

        List<Long> candidateIds = postulations.stream()
                .map(PostulationEntity::getCandidate)
                .filter(Objects::nonNull)
                .map(CandidateEntity::getId)
                .toList();

        List<CandidateEntity> candidates = candidateRepository.findByIdIn(candidateIds);

        return candidates.stream()
                .map(mapperCandidateResponse::toDto)
                .collect(Collectors.toList());
    }

    public List<CandidateResponseDto> getByNameCandidate(String name){
        List<CandidateEntity> candidateEntities = candidateRepository.findByName(name);

        if(candidateEntities.isEmpty()){
            throw new CandidateNoExistException();
        }

        return candidateEntities.stream()
                .map(mapperCandidateResponse::toDto)
                .collect(Collectors.toList());
    }

    public CandidateResponseDto getByIdCandidate(Long id){
        return candidateRepository.findById(id)
                .map(mapperCandidateResponse::toDto)
                .orElseThrow(CandidateNoExistException::new);
    }

    public List<CandidateResponseDto> getAllCandidate(){
        return candidateRepository.findAll().stream()
                .map(mapperCandidateResponse::toDto)
                .collect(Collectors.toList());
    }

    public CandidateResponseDto saveCandidate(CandidateRequestDto candidateRequestDto) {
        if(candidateRepository.existsByCard(candidateRequestDto.getCard())){
            throw new IdCardAlreadyExistException();
        }
        if(candidateRepository.existsByPhone(candidateRequestDto.getPhone())){
            throw new PhoneAlreadyExistException();
        }
        CandidateEntity candidateEntity = mapperCandidateRequest.toEntity(candidateRequestDto);
        CandidateEntity candidateEntitySave = candidateRepository.save(candidateEntity);
        return mapperCandidateResponse.toDto(candidateEntitySave);
    }

    public Optional<CandidateResponseDto> updateCandidate(Long id, CandidateRequestDto candidateRequestDto) {
        return candidateRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(candidateRequestDto.getName());
                    existingEntity.setLastName(candidateRequestDto.getLastName());
                    existingEntity.setCard(candidateRequestDto.getCard());
                    existingEntity.setBirthdate(candidateRequestDto.getBirthdate());
                    existingEntity.setPhone(candidateRequestDto.getPhone());
                    existingEntity.setCity(candidateRequestDto.getCity());
                    existingEntity.setEmail(candidateRequestDto.getEmail());

                    return mapperCandidateResponse.toDto(candidateRepository.save(existingEntity));
                });
    }

    public boolean deleteCandidate(Long id){
        if (candidateRepository.existsById(id)) {
            candidateRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
