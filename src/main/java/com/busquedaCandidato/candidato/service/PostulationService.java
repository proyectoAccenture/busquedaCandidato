package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.PostulationRequestDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperPostulationResponse;
import com.busquedaCandidato.candidato.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostulationService {
    private final IPostulationRepository postulationRepository;
    private final IVacancyCompanyRepository vacancyCompanyRepository;
    private final ICandidateRepository candidateRepository;
    private final IMapperPostulationResponse mapperPostulationResponse;

    public PostulationResponseDto getPostulation(Long id){
        PostulationEntity postulationEntity = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        return mapperPostulationResponse.toDto(postulationEntity);
    }

    public List<PostulationResponseDto> getAllPostulation(){
        return postulationRepository.findAll().stream()
                .map(mapperPostulationResponse::toDto)
                .collect(Collectors.toList());
    }

    public PostulationResponseDto savePostulation(PostulationRequestDto postulationRequestDto) {
        VacancyCompanyEntity vacancyCompanyEntity = vacancyCompanyRepository.findById(postulationRequestDto.getVacancyCompanyId())
                .orElseThrow(EntityNoExistException::new);

        CandidateEntity candidateEntity = candidateRepository.findById(postulationRequestDto.getCandidateId())
                .orElseThrow(EntityNoExistException::new);

        if (!postulationRequestDto.getStatus()) {
            throw new IllegalStateException("No se puede postular, ya que la postulación está inactiva.");
        }

        boolean alreadyApplied = postulationRepository
                .existsByCandidate_IdAndVacancyCompany_IdAndStatus(candidateEntity.getId(), vacancyCompanyEntity.getId(), true);

        if (alreadyApplied) {
            throw new IllegalStateException("Ya realizaste una postulación activa para esta vacante.");
        }

        PostulationEntity postulationEntityNew = new PostulationEntity();
        postulationEntityNew.setDatePresentation(postulationRequestDto.getDatePresentation());
        postulationEntityNew.setSalaryAspiration(postulationRequestDto.getSalaryAspiration());
        postulationEntityNew.setVacancyCompany(vacancyCompanyEntity);
        postulationEntityNew.setCandidate(candidateEntity);
        postulationEntityNew.setStatus(true);

        PostulationEntity postulationEntitySave = postulationRepository.save(postulationEntityNew);
        return mapperPostulationResponse.toDto(postulationEntitySave);
    }

    public Optional<PostulationResponseDto> updatePostulation(Long id, PostulationRequestDto postulationRequestDto) {
        PostulationEntity existingEntity  = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        VacancyCompanyEntity vacancyCompanyEntity = vacancyCompanyRepository.findById(postulationRequestDto.getVacancyCompanyId())
                .orElseThrow(EntityNoExistException::new);

        CandidateEntity candidateEntity = candidateRepository.findById(postulationRequestDto.getCandidateId())
                .orElseThrow(EntityNoExistException::new);

        existingEntity.setDatePresentation(postulationRequestDto.getDatePresentation());
        existingEntity.setSalaryAspiration(postulationRequestDto.getSalaryAspiration());
        existingEntity.setVacancyCompany(vacancyCompanyEntity);
        existingEntity.setCandidate(candidateEntity);

        return Optional.of(mapperPostulationResponse.toDto(postulationRepository.save(existingEntity)));
    }

    public void deletePostulation(Long id){
        PostulationEntity existingPostulation = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        postulationRepository.delete(existingPostulation);
    }
}
