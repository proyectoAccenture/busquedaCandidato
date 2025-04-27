package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CompanyVacancyRequestDto;
import com.busquedaCandidato.candidato.dto.response.CompanyVacancyResponseDto;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.entity.CompanyVacancyEntity;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperVacancyCompany;
import com.busquedaCandidato.candidato.repository.ICompanyVacancyRepository;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
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
    private final IMapperVacancyCompany mapperVacancyCompany;

    public CompanyVacancyResponseDto getVacancyCompany(Long id){
        return vacancyCompanyRepository.findById(id)
                .map(mapperVacancyCompany::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<CompanyVacancyResponseDto> getAllVacancyCompany(){
        return vacancyCompanyRepository.findAll().stream()
                .map(mapperVacancyCompany::toDto)
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
        companyVacancyEntityNew.setRole(null);

        CompanyVacancyEntity vacancyEntitySave = vacancyCompanyRepository.save(companyVacancyEntityNew);

        jobProfileEntity.getVacancies().add(vacancyEntitySave);
        jobProfileRepository.save(jobProfileEntity);

        originEntity.getVacancies().add(vacancyEntitySave);
        originRepository.save(originEntity);

        return mapperVacancyCompany.toDto(vacancyEntitySave);
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
        existingEntity.setRole(null);

        CompanyVacancyEntity vacancyEntitySave = vacancyCompanyRepository.save(existingEntity);

        jobProfileEntity.getVacancies().add(vacancyEntitySave);
        jobProfileRepository.save(jobProfileEntity);

        originEntity.getVacancies().add(vacancyEntitySave);
        originRepository.save(originEntity);

        return Optional.of(mapperVacancyCompany.toDto(vacancyEntitySave));
    }

    public void deleteVacancyCompany(Long id){
        CompanyVacancyEntity existingVacancyCompany = vacancyCompanyRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        vacancyCompanyRepository.delete(existingVacancyCompany);
    }
}
