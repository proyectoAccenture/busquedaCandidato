package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.exception.type.*;
import com.busquedaCandidato.candidato.mapper.*;
import com.busquedaCandidato.candidato.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    private final IMapperCandidateResponse mapperCandidateResponse;
    private final IMapperCandidateRequest mapperCandidateRequest;

    public Optional<CandidateResponseDto> getCandidate(Long id){
        return candidateRepository.findById(id)
                .map(mapperCandidateResponse::CandidateToCandidateResponse);
    }

    public List<CandidateResponseDto> getAllCandidate(){
        return candidateRepository.findAll().stream()
                .map(mapperCandidateResponse::CandidateToCandidateResponse)
                .collect(Collectors.toList());
    }

    public CandidateResponseDto saveCandidate(CandidateRequestDto candidateRequestDto) {
        if(candidateRepository.existsByCard(candidateRequestDto.getCard())){
            throw new IdCardAlreadyExistException();
        }
        CandidateEntity candidateEntity = mapperCandidateRequest.CandidateRequestToCandidate(candidateRequestDto);
        CandidateEntity candidateEntitySave = candidateRepository.save(candidateEntity);
        return mapperCandidateResponse.CandidateToCandidateResponse(candidateEntitySave);
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

                    return mapperCandidateResponse.CandidateToCandidateResponse(candidateRepository.save(existingEntity));
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
