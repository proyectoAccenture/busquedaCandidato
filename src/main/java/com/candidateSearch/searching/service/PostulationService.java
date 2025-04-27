package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.PostulationRequestDto;
import com.candidateSearch.searching.dto.response.PostulationResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.CannotApplyException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.ItAlreadyExistPostulationException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import com.candidateSearch.searching.mapper.IMapperPostulation;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostulationService {
    private final IPostulationRepository postulationRepository;
    private final ICandidateRepository candidateRepository;
    private final IMapperPostulation mapperPostulation;
    private final IRoleRepository roleIDRepository;

    public PostulationResponseDto getPostulation(Long id){
        PostulationEntity postulationEntity = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        return mapperPostulation.toDto(postulationEntity);
    }

    public List<PostulationResponseDto> getAllPostulation(){
        return postulationRepository.findAll().stream()
                .filter(PostulationEntity::getStatus)
                .map(mapperPostulation::toDto)
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
                .map(mapperPostulation::toDto)
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
                .map(mapperPostulation::toDto)
                .collect(Collectors.toList());

    }

    public PostulationResponseDto savePostulation(PostulationRequestDto postulationRequestDto) {

        if (!postulationRequestDto.getStatus()) {
            throw new CannotApplyException();
        }

        RoleEntity roleEntity = roleIDRepository.findById(postulationRequestDto.getRoleId())
                .orElseThrow(EntityNoExistException::new);

        CandidateEntity candidateEntity = candidateRepository.findById(postulationRequestDto.getCandidateId())
                .orElseThrow(EntityNoExistException::new);

        boolean alreadyApplied = postulationRepository
                .existsByCandidate_IdAndRole_IdAndStatus(candidateEntity.getId(), roleEntity.getId(), true);

        if (alreadyApplied) {
            throw new ItAlreadyExistPostulationException();
        }

        PostulationEntity postulationEntityNew = new PostulationEntity();
        postulationEntityNew.setStatus(postulationRequestDto.getStatus());
        postulationEntityNew.setDatePresentation(postulationRequestDto.getDatePresentation());
        postulationEntityNew.setRole(roleEntity);
        postulationEntityNew.setCandidate(candidateEntity);
        postulationEntityNew.setProcess(null);
        PostulationEntity postulationEntitySave = postulationRepository.save(postulationEntityNew);

        candidateEntity.getPostulations().add(postulationEntitySave);
        candidateRepository.save(candidateEntity);

        roleEntity.getPostulations().add(postulationEntitySave);
        roleIDRepository.save(roleEntity);

        return mapperPostulation.toDto(postulationEntitySave);
    }

    public Optional<PostulationResponseDto> updatePostulation(Long id, PostulationRequestDto postulationRequestDto) {

        PostulationEntity existingEntity  = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        RoleEntity roleEntity = roleIDRepository.findById(postulationRequestDto.getRoleId())
                .orElseThrow(EntityNoExistException::new);

        CandidateEntity candidateEntity = candidateRepository.findById(postulationRequestDto.getCandidateId())
                .orElseThrow(EntityNoExistException::new);

        boolean alreadyApplied = postulationRepository
                .existsByCandidate_IdAndRole_IdAndStatus(candidateEntity.getId(), roleEntity.getId(), true);

        if (alreadyApplied &&
                (!existingEntity.getCandidate().getId().equals(candidateEntity.getId()) ||
                        !existingEntity.getRole().getId().equals(roleEntity.getId()) ||
                        !existingEntity.getStatus())) {

            throw new ItAlreadyExistPostulationException();
        }

        existingEntity.setDatePresentation(postulationRequestDto.getDatePresentation());
        existingEntity.setRole(roleEntity);
        existingEntity.setStatus(postulationRequestDto.getStatus());
        existingEntity.setCandidate(candidateEntity);

        PostulationEntity postulationEntitySave = postulationRepository.save(existingEntity);

        candidateEntity.getPostulations().add(postulationEntitySave);
        candidateRepository.save(candidateEntity);

        roleEntity.getPostulations().add(postulationEntitySave);
        roleIDRepository.save(roleEntity);

        return Optional.of(mapperPostulation.toDto(postulationEntitySave));
    }

    public void deletePostulation(long id) {
        PostulationEntity postulation = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        postulationRepository.delete(postulation);
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
