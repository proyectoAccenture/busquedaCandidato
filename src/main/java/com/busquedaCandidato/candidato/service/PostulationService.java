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
        return mapperPostulationResponse.PostulationToPostulationResponse(postulationEntity);
    }

    public List<PostulationResponseDto> getAllPostulation(){
        return postulationRepository.findAll().stream()
                .map(mapperPostulationResponse::PostulationToPostulationResponse)
                .collect(Collectors.toList());
    }

    public PostulationResponseDto savePostulation(PostulationRequestDto postulationRequestDto) {
        VacancyCompanyEntity vacancyCompanyEntity = vacancyCompanyRepository.findById(postulationRequestDto.getVacancyCompanyId())
                .orElseThrow(EntityNoExistException::new);

        CandidateEntity candidateEntity = candidateRepository.findById(postulationRequestDto.getCandidateId())
                .orElseThrow(EntityNoExistException::new);

        PostulationEntity postulationEntityNew = new PostulationEntity();
        postulationEntityNew.setDatePresentation(postulationRequestDto.getDatePresentation());
        postulationEntityNew.setSalaryAspiration(postulationRequestDto.getSalaryAspiration());
        postulationEntityNew.setVacancyCompany(vacancyCompanyEntity);
        postulationEntityNew.setCandidate(candidateEntity);


        PostulationEntity postulationEntitySave = postulationRepository.save(postulationEntityNew);
        return mapperPostulationResponse.PostulationToPostulationResponse(postulationEntitySave);
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

                    return Optional.of(mapperPostulationResponse.PostulationToPostulationResponse(postulationRepository.save(existingEntity)));

    }

    public void deletePostulation(Long id){
        PostulationEntity existingPostulation = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);
        postulationRepository.delete(existingPostulation);
    }
}
