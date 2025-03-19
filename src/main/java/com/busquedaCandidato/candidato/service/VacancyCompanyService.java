package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.VacancyCompanyRequestDto;
import com.busquedaCandidato.candidato.dto.response.VacancyCompanyResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperVacancyCompanyResponse;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VacancyCompanyService {
    private final IVacancyCompanyRepository vacancyCompanyRepository;
    private final IRoleIDRepository roleIDRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IMapperVacancyCompanyResponse mapperVacancyCompanyResponse;

    public VacancyCompanyResponseDto getVacancyCompany(Long id){
        return vacancyCompanyRepository.findById(id)
                .map(mapperVacancyCompanyResponse::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<VacancyCompanyResponseDto> getAllVacancyCompany(){
        return vacancyCompanyRepository.findAll().stream()
                .map(mapperVacancyCompanyResponse::toDto)
                .collect(Collectors.toList());
    }

    public VacancyCompanyResponseDto saveVacancyCompany(VacancyCompanyRequestDto vacancyCompanyRequestDto) {
        RoleIDEntity roleIDEntity = roleIDRepository.findById(vacancyCompanyRequestDto.getRole())
                .orElseThrow(EntityNoExistException::new);

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(vacancyCompanyRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(vacancyCompanyRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        VacancyCompanyEntity vacancyCompanyEntityNew = new VacancyCompanyEntity();
        vacancyCompanyEntityNew.setContract(vacancyCompanyRequestDto.getContract());
        vacancyCompanyEntityNew.setSalary(vacancyCompanyRequestDto.getSalary());
        vacancyCompanyEntityNew.setExperience(vacancyCompanyRequestDto.getExperience());
        vacancyCompanyEntityNew.setLevel(vacancyCompanyRequestDto.getLevel());
        vacancyCompanyEntityNew.setSkills(vacancyCompanyRequestDto.getSkills());
        vacancyCompanyEntityNew.setDescription(vacancyCompanyRequestDto.getDescription());
        vacancyCompanyEntityNew.setDatePublication(vacancyCompanyRequestDto.getDatePublication());
        vacancyCompanyEntityNew.setSource(vacancyCompanyRequestDto.getSource());
        vacancyCompanyEntityNew.setRole(roleIDEntity);
        vacancyCompanyEntityNew.setJobProfile(jobProfileEntity);
        vacancyCompanyEntityNew.setOrigin(originEntity);

        VacancyCompanyEntity vacancyCompanyEntitySave = vacancyCompanyRepository.save(vacancyCompanyEntityNew);
        return mapperVacancyCompanyResponse.toDto(vacancyCompanyEntitySave);
    }

    public Optional<VacancyCompanyResponseDto> updateVacancyCompany(Long id, VacancyCompanyRequestDto vacancyCompanyRequestDto) {
        VacancyCompanyEntity existingEntity  = vacancyCompanyRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        RoleIDEntity roleIDEntity = roleIDRepository.findById(vacancyCompanyRequestDto.getRole())
                .orElseThrow(EntityNoExistException::new);

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(vacancyCompanyRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(vacancyCompanyRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        existingEntity.setContract(vacancyCompanyRequestDto.getContract());
        existingEntity.setSalary(vacancyCompanyRequestDto.getSalary());
        existingEntity.setExperience(vacancyCompanyRequestDto.getExperience());
        existingEntity.setLevel(vacancyCompanyRequestDto.getLevel());
        existingEntity.setSkills(vacancyCompanyRequestDto.getSkills());
        existingEntity.setDescription(vacancyCompanyRequestDto.getDescription());
        existingEntity.setDatePublication(vacancyCompanyRequestDto.getDatePublication());
        existingEntity.setSource(vacancyCompanyRequestDto.getSource());
        existingEntity.setRole(roleIDEntity);
        existingEntity.setJobProfile(jobProfileEntity);
        existingEntity.setOrigin(originEntity);

        return Optional.of(mapperVacancyCompanyResponse.toDto(vacancyCompanyRepository.save(existingEntity)));
    }

    public void deleteVacancyCompany(Long id){
        VacancyCompanyEntity existingVacancyCompany = vacancyCompanyRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        vacancyCompanyRepository.delete(existingVacancyCompany);
    }
}
