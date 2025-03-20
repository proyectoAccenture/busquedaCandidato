package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.PostulationRequestDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.exception.type.*;
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
    private final IProcessRepository processRepository;
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

    public List<PostulationResponseDto> getSearchPostulationsByCandidate(String query) {
        validationQuery(query);

        List<PostulationEntity> postulations = postulationRepository.searchByCandidateNameOrLastName(query);
        validationListPostulation(postulations);

        return postulations.stream()
                .map(mapperPostulationResponse::toDto)
                .collect(Collectors.toList());
    }

    public PostulationResponseDto savePostulation(PostulationRequestDto postulationRequestDto) {
        VacancyCompanyEntity vacancyCompanyEntity = vacancyCompanyRepository.findById(postulationRequestDto.getVacancyCompanyId())
                .orElseThrow(EntityNoExistException::new);

        CandidateEntity candidateEntity = candidateRepository.findById(postulationRequestDto.getCandidateId())
                .orElseThrow(EntityNoExistException::new);

        if (!postulationRequestDto.getStatus()) {
            throw new CannotApplyException();
        }

        boolean alreadyApplied = postulationRepository
                .existsByCandidate_IdAndVacancyCompany_IdAndStatus(candidateEntity.getId(), vacancyCompanyEntity.getId(), true);

        if (alreadyApplied) {
            throw new ItAlreadyExistPostulationException();
        }

        PostulationEntity postulationEntityNew = new PostulationEntity();
        postulationEntityNew.setDatePresentation(postulationRequestDto.getDatePresentation());
        postulationEntityNew.setSalaryAspiration(postulationRequestDto.getSalaryAspiration());
        postulationEntityNew.setVacancyCompany(vacancyCompanyEntity);
        postulationEntityNew.setCandidate(candidateEntity);
        postulationEntityNew.setStatus(postulationRequestDto.getStatus());

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
        existingEntity.setStatus(postulationRequestDto.getStatus());
        existingEntity.setCandidate(candidateEntity);

        return Optional.of(mapperPostulationResponse.toDto(postulationRepository.save(existingEntity)));
    }

    public void deletePostulation(Long id){
        PostulationEntity existingPostulation = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        if (processRepository.existsByPostulationId(id)) {
            throw new EntityAlreadyHasRelationException();
        }

        postulationRepository.delete(existingPostulation);
    }

    private void validationQuery(String query){
        if (query == null || query.trim().isEmpty()) {
            throw new BadRequestException("The search query cannot be empty.");
        }
    }

    private void validationListPostulation(List<PostulationEntity> postulations){
        if (postulations.isEmpty()) {
            throw new ResourceNotFoundException("No postulations found for the given search criteria.");
        }
    }
}
