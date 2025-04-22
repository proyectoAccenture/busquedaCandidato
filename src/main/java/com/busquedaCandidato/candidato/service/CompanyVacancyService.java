package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CompanyVacancyRequestDto;
import com.busquedaCandidato.candidato.dto.response.CompanyVacancyResponseDto;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.entity.CompanyVacancyEntity;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyHasRelationException;
import com.busquedaCandidato.candidato.mapper.IMapperVacancyCompanyResponse;
import com.busquedaCandidato.candidato.repository.ICompanyVacancyRepository;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyVacancyService {
    private final ICompanyVacancyRepository vacancyCompanyRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IPostulationRepository postulationRepository;
    private final IMapperVacancyCompanyResponse mapperVacancyCompanyResponse;

    public CompanyVacancyResponseDto getVacancyCompany(Long id){
        return vacancyCompanyRepository.findById(id)
                .map(mapperVacancyCompanyResponse::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<CompanyVacancyResponseDto> getAllVacancyCompany(){
        return vacancyCompanyRepository.findAll().stream()
                .map(mapperVacancyCompanyResponse::toDto)
                .collect(Collectors.toList());
    }

    public CompanyVacancyResponseDto saveVacancyCompany(CompanyVacancyRequestDto companyVacancyRequestDto) {
        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(companyVacancyRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(companyVacancyRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        CompanyVacancyEntity companyVacancyEntityNew = new CompanyVacancyEntity();
        companyVacancyEntityNew.setDescription(companyVacancyRequestDto.getDescription());
        companyVacancyEntityNew.setContract(companyVacancyRequestDto.getContract());
        companyVacancyEntityNew.setSalary(companyVacancyRequestDto.getSalary());
        companyVacancyEntityNew.setLevel(companyVacancyRequestDto.getLevel());
        companyVacancyEntityNew.setSeniority(companyVacancyRequestDto.getSeniority());
        companyVacancyEntityNew.setSkills(companyVacancyRequestDto.getSkills());
        companyVacancyEntityNew.setExperience(companyVacancyRequestDto.getExperience());
        companyVacancyEntityNew.setAssignmentTime(companyVacancyRequestDto.getAssignmentTime());
        companyVacancyEntityNew.setJobProfile(jobProfileEntity);
        companyVacancyEntityNew.setOrigin(originEntity);

        CompanyVacancyEntity companyVacancyEntitySave = vacancyCompanyRepository.save(companyVacancyEntityNew);
        return mapperVacancyCompanyResponse.toDto(companyVacancyEntitySave);
    }

    public Optional<CompanyVacancyResponseDto> updateVacancyCompany(Long id, CompanyVacancyRequestDto companyVacancyRequestDto) {
        CompanyVacancyEntity existingEntity  = vacancyCompanyRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(companyVacancyRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(companyVacancyRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        existingEntity.setDescription(companyVacancyRequestDto.getDescription());
        existingEntity.setContract(companyVacancyRequestDto.getContract());
        existingEntity.setSalary(companyVacancyRequestDto.getSalary());
        existingEntity.setLevel(companyVacancyRequestDto.getLevel());
        existingEntity.setSeniority(companyVacancyRequestDto.getSeniority());
        existingEntity.setSkills(companyVacancyRequestDto.getSkills());
        existingEntity.setExperience(companyVacancyRequestDto.getExperience());
        existingEntity.setAssignmentTime(companyVacancyRequestDto.getAssignmentTime());
        existingEntity.setJobProfile(jobProfileEntity);
        existingEntity.setOrigin(originEntity);

        return Optional.of(mapperVacancyCompanyResponse.toDto(vacancyCompanyRepository.save(existingEntity)));
    }

    public void deleteVacancyCompany(Long id){
        CompanyVacancyEntity existingVacancyCompany = vacancyCompanyRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        if (postulationRepository.existsByRoleId(id)) {
            throw new EntityAlreadyHasRelationException();
        }

        vacancyCompanyRepository.delete(existingVacancyCompany);
    }
}
