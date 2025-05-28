package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.CandidateResumeResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.exception.globalmessage.GlobalMessage;
import com.candidateSearch.searching.exception.type.BusinessException;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.InvalidFileTypeException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import com.candidateSearch.searching.mapper.IMapperCandidate;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.service.operationsbusiness.candidate.OperationCandidate;
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
    private final OperationCandidate operationCandidate;

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
            if (candidateRequestDto.getStatus() == Status.INACTIVE ||
                    candidateRequestDto.getStatus() == Status.BLOCKED) {
                throw new BusinessException(GlobalMessage.CANNOT_BE_CREATED);
            }

            operationCandidate.validateUniqueFields(candidateRequestDto);

            JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                    .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

            OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                    .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

            CandidateEntity toSave = operationCandidate.buildCandidate(candidateRequestDto, jobProfileEntity, originEntity);

            if (file != null && !file.isEmpty()) {
                if (!Objects.equals(file.getContentType(), "application/pdf")) {
                    throw new InvalidFileTypeException("The file must be a PDF");
                }

                toSave.setResumePdf(file.getBytes());
                toSave.setResumeFileName(file.getOriginalFilename());
                toSave.setResumeContentType(file.getContentType());
            }
            CandidateEntity saved = candidateRepository.save(toSave);
            CandidateResponseDto responseDto = mapperCandidate.toDto(saved);

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

