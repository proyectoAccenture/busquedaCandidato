package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.CandidateResumeResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.InvalidFileTypeException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import com.candidateSearch.searching.exception.type.FieldAlreadyExistException;
import com.candidateSearch.searching.mapper.IMapperCandidate;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.utility.Status;
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
    private final IMapperCandidate mapperCandidate;

    @Transactional
    public void uploadResume(Long candidateId, MultipartFile file) {
        try {

            if (file.isEmpty()) {
                throw new BadRequestException("The file is empty");
            }

            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                throw new InvalidFileTypeException("The file must be a PDF");
            }

            CandidateEntity candidate = candidateRepository.findById(candidateId)
                    .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + candidateId));

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
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + candidateId));

        if (candidate.getResumePdf() == null) {
            throw new ResourceNotFoundException("The candidate does not have a resume uploaded");
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
            if (candidateRepository.existsByCardAndStatusNot(candidateRequestDto.getCard(), Status.INACTIVE)) {
                throw new FieldAlreadyExistException("card");
            }

            if (candidateRepository.existsByPhoneAndStatusNot(candidateRequestDto.getPhone(), Status.INACTIVE)) {
                throw new FieldAlreadyExistException("phone");
            }

            JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                    .orElseThrow(EntityNoExistException::new);

            OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                    .orElseThrow(EntityNoExistException::new);

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
            candidateEntityNew.setOrigin(originEntity);
            candidateEntityNew.setJobProfile(jobProfileEntity);

            if (file != null && !file.isEmpty()) {
                if (!Objects.equals(file.getContentType(), "application/pdf")) {
                    throw new InvalidFileTypeException("The file must be a PDF");
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

