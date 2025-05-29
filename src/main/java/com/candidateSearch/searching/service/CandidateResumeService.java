package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.CandidateResumeResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.exception.type.*;
import com.candidateSearch.searching.mapper.IMapperCandidate;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.entity.utility.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateResumeService {
    private final ICandidateRepository candidateRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IMapperCandidate mapperCandidate;

    @Transactional
    public void uploadResume(Long candidateId, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new CustomBadRequestException("The file is empty");
            }

            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                throw new CustomBadRequestException("The file must be a PDF");
            }

            CandidateEntity candidate = candidateRepository.findById(candidateId)
                    .orElseThrow(() -> new CustomNotFoundException("Candidate not found with ID: " + candidateId));

            candidate.setResumePdf(file.getBytes());
            candidate.setResumeFileName(file.getOriginalFilename());
            candidate.setResumeContentType(file.getContentType());
            candidateRepository.save(candidate);

        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public CandidateResumeResponseDto getResume(Long candidateId) {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new CustomNotFoundException("Candidate not found with ID: " + candidateId));

        if (candidate.getResumePdf() == null) {
            throw new CustomNotFoundException("The candidate does not have a resume uploaded.");
        }

        return new CandidateResumeResponseDto(
                candidate.getId(),
                candidate.getResumeFileName(),
                candidate.getResumeContentType(),
                candidate.getResumePdf()
        );
    }

    @Transactional
    public CandidateResponseDto saveCandidateWithResume(CandidateRequestDto candidateRequestDto, MultipartFile file) {
        try {
            if (candidateRequestDto.getStatus() == Status.INACTIVE ||
                    candidateRequestDto.getStatus() == Status.BLOCKED) {
                throw new CustomBadRequestException("Cannot be create or update, valid the status.");
            }

            Optional<CandidateEntity> existingOptCard = candidateRepository.findByCardAndStatusNot(candidateRequestDto.getCard(), Status.INACTIVE);

            if(existingOptCard.isPresent()){
                CandidateEntity existing = existingOptCard.get();

                if (existing.getStatus() == Status.BLOCKED) {
                    throw new CustomConflictException("Candidate is blocked.");
                } else {
                    throw new CustomConflictException("There is already a candidate with that card.");
                }
            }

            Optional<CandidateEntity> existingOptPhone = candidateRepository.findByPhoneAndStatusNot(candidateRequestDto.getPhone(), Status.INACTIVE);

            if(existingOptPhone.isPresent()){
                CandidateEntity existing = existingOptPhone.get();

                if (existing.getStatus() == Status.BLOCKED) {
                    throw new CustomConflictException("Candidate is blocked.");
                } else {
                    throw new CustomConflictException("That phone number already exists");
                }
            }

            Optional<CandidateEntity> existingOptEmail = candidateRepository.findByEmailAndStatusNot(candidateRequestDto.getEmail(), Status.INACTIVE);

            if(existingOptEmail.isPresent()){
                CandidateEntity existing = existingOptEmail.get();

                if (existing.getStatus() == Status.BLOCKED) {
                    throw new CustomConflictException("Candidate is blocked.");
                } else {
                    throw new CustomConflictException("that email address already exists.");
                }
            }

            JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                    .orElseThrow(()-> new CustomNotFoundException("There is no job profile with that ID."));

            OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                    .orElseThrow(()-> new CustomNotFoundException("There is no origin with that ID."));

            CandidateEntity candidateEntityNew = new CandidateEntity();
            candidateEntityNew.setName(candidateRequestDto.getName());
            candidateEntityNew.setLastName(candidateRequestDto.getLastName());
            candidateEntityNew.setCard(candidateRequestDto.getCard());
            candidateEntityNew.setPhone(candidateRequestDto.getPhone());
            candidateEntityNew.setCity(candidateRequestDto.getCity());
            candidateEntityNew.setEmail(candidateRequestDto.getEmail());
            candidateEntityNew.setBirthdate(candidateRequestDto.getBirthdate());
            candidateEntityNew.setSource(candidateRequestDto.getSource());
            candidateEntityNew.setSkills(candidateRequestDto.getSkills());
            candidateEntityNew.setYearsExperience(candidateRequestDto.getYearsExperience());
            candidateEntityNew.setWorkExperience(candidateRequestDto.getWorkExperience());
            candidateEntityNew.setSeniority(candidateRequestDto.getSeniority());
            candidateEntityNew.setSalaryAspiration(candidateRequestDto.getSalaryAspiration());
            candidateEntityNew.setLevel(candidateRequestDto.getLevel());
            candidateEntityNew.setDatePresentation(candidateRequestDto.getDatePresentation());
            candidateEntityNew.setStatus(candidateRequestDto.getStatus());
            candidateEntityNew.setOrigin(originEntity);
            candidateEntityNew.setJobProfile(jobProfileEntity);

            if (file != null && !file.isEmpty()) {
                if (!Objects.equals(file.getContentType(), "application/pdf")) {
                    throw new CustomBadRequestException("The file must be a PDF");
                }

                candidateEntityNew.setResumePdf(file.getBytes());
                candidateEntityNew.setResumeFileName(file.getOriginalFilename());
                candidateEntityNew.setResumeContentType(file.getContentType());
            }
            CandidateEntity candidateEntitySave = candidateRepository.save(candidateEntityNew);
            CandidateResponseDto responseDto = mapperCandidate.toDto(candidateEntitySave);

            if (file != null && !file.isEmpty()) {
                responseDto.setHasResume(true);
                responseDto.setResumeFileName(file.getOriginalFilename());
            } else {
                responseDto.setHasResume(false);
            }
            return responseDto;

        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + e.getMessage());
        }
    }
}

