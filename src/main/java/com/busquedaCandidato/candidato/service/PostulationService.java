package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.PostulationRequestDto;
import com.busquedaCandidato.candidato.dto.response.PostulationResponseDto;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.CannotApplyException;
import com.busquedaCandidato.candidato.exception.type.BadRequestException;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyHasRelationException;
import com.busquedaCandidato.candidato.exception.type.ItAlreadyExistPostulationException;
import com.busquedaCandidato.candidato.exception.type.ResourceNotFoundException;
import com.busquedaCandidato.candidato.mapper.IMapperPostulationResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
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

    public List<PostulationResponseDto> getSearchPostulationsByCandidateFullName(String query) {
        validateStringQuery(query);
        query = normalizeQuery(query);

        String[] words = query.split(" ");
        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;

        List<PostulationEntity> postulations = postulationRepository.searchByCandidateNameOrLastName(word1, word2, word3, word4);
        validationListPostulation(postulations);

        return postulations.stream()
                .map(mapperPostulationResponse::toDto)
                .collect(Collectors.toList());
    }

    public List<PostulationResponseDto> searchByCandidateNameLastNameAndRole(String query) {
        validateStringQuery(query);
        query = normalizeQuery(query);

        String[] words = query.split(" ");
        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;

        List<PostulationEntity> postulations = postulationRepository.searchByCandidateNameLastNameAndRole(word1, word2, word3, word4, query);
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

    private void validationListPostulation(List<PostulationEntity> postulations){
        if (postulations.isEmpty()) {
            throw new ResourceNotFoundException("No postulations found for the given search criteria.");
        }
    }
    private void validateStringQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new BadRequestException("The search query cannot be empty.");
        }
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return query;
        }
        query = query.trim();
        return query.replaceAll("[áÁ]", "a")
                .replaceAll("[éÉ]", "e")
                .replaceAll("[íÍ]", "i")
                .replaceAll("[óÓ]", "o")
                .replaceAll("[úÚ]", "u");
    }
}
