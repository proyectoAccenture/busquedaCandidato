package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateResumeRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResumeResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.BadRequestException;
import com.busquedaCandidato.candidato.exception.type.InvalidFileTypeException;
import com.busquedaCandidato.candidato.exception.type.ResourceNotFoundException;
import com.busquedaCandidato.candidato.exception.type.IdCardAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.PhoneAlreadyExistException;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CandidateResumeService {
    private final ICandidateRepository candidateRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IMapperCandidateResponse mapperCandidateResponse;

    @Transactional
    public void uploadResume(Long candidateId, MultipartFile file) {
        try {

            if (file.isEmpty()) {
                throw new BadRequestException("El archivo está vacío");
            }

            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                throw new InvalidFileTypeException("El archivo debe ser un PDF");
            }

            CandidateEntity candidate = candidateRepository.findById(candidateId)
                    .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con ID: " + candidateId));

            candidate.setResumePdf(file.getBytes());
            candidate.setResumeFileName(file.getOriginalFilename());
            candidate.setResumeContentType(file.getContentType());
            candidateRepository.save(candidate);

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public CandidateResumeResponseDto getResume(Long candidateId) {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con ID: " + candidateId));

        if (candidate.getResumePdf() == null) {
            throw new ResourceNotFoundException("El candidato no tiene hoja de vida cargada");
        }

        return new CandidateResumeResponseDto(
                candidate.getId(),
                candidate.getResumeFileName(),
                candidate.getResumeContentType(),
                candidate.getResumePdf()
        );
    }

    @Transactional
    public CandidateResponseDto saveCandidateWithResume(CandidateResumeRequestDto candidateResumeRequestDto, MultipartFile file) {
        try {
            if(candidateRepository.existsByCard(candidateResumeRequestDto.getCard())){
                throw new IdCardAlreadyExistException();
            }

            if(candidateRepository.existsByPhone(candidateResumeRequestDto.getPhone())){
                throw new PhoneAlreadyExistException();
            }

            JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateResumeRequestDto.getJobProfile())
                    .orElseThrow(EntityNoExistException::new);

            OriginEntity originEntity = originRepository.findById(candidateResumeRequestDto.getOrigin())
                    .orElseThrow(EntityNoExistException::new);

            CandidateEntity candidateEntityNew = new CandidateEntity();
            candidateEntityNew.setName(candidateResumeRequestDto.getName());
            candidateEntityNew.setLastName(candidateResumeRequestDto.getLastName());
            candidateEntityNew.setCard(candidateResumeRequestDto.getCard());
            candidateEntityNew.setPhone(candidateResumeRequestDto.getPhone());
            candidateEntityNew.setCity(candidateResumeRequestDto.getCity());
            candidateEntityNew.setEmail(candidateResumeRequestDto.getEmail());
            candidateEntityNew.setBirthdate(candidateResumeRequestDto.getBirthdate());
            candidateEntityNew.setSource(candidateResumeRequestDto.getSource());
            candidateEntityNew.setSkills(candidateResumeRequestDto.getSkills());
            candidateEntityNew.setYearsExperience(candidateResumeRequestDto.getYearsExperience());
            candidateEntityNew.setWorkExperience(candidateResumeRequestDto.getWorkExperience());
            candidateEntityNew.setSeniority(candidateResumeRequestDto.getSeniority());
            candidateEntityNew.setSalaryAspiration(candidateResumeRequestDto.getSalaryAspiration());
            candidateEntityNew.setLevel(candidateResumeRequestDto.getLevel());
            candidateEntityNew.setDatePresentation(candidateResumeRequestDto.getDatePresentation());
            candidateEntityNew.setOrigin(originEntity);
            candidateEntityNew.setJobProfile(jobProfileEntity);

            if (file != null && !file.isEmpty()) {
                if (!Objects.equals(file.getContentType(), "application/pdf")) {
                    throw new InvalidFileTypeException("El archivo debe ser un PDF");
                }

                candidateEntityNew.setResumePdf(file.getBytes());
                candidateEntityNew.setResumeFileName(file.getOriginalFilename());
                candidateEntityNew.setResumeContentType(file.getContentType());
            }
            CandidateEntity candidateEntitySave = candidateRepository.save(candidateEntityNew);
            CandidateResponseDto responseDto = mapperCandidateResponse.toDto(candidateEntitySave);

            if (file != null && !file.isEmpty()) {
                responseDto.setHasResume(true);
                responseDto.setResumeFileName(file.getOriginalFilename());
            } else {
                responseDto.setHasResume(false);
            }
            return responseDto;

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage());
        }
    }
}

