package com.candidateSearch.searching.service;

import java.util.List;
import java.util.stream.Collectors;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.exception.type.CustomConflictException;
import com.candidateSearch.searching.exception.type.CustomNotFoundException;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.candidateSearch.searching.dto.request.OriginRequestDto;
import com.candidateSearch.searching.dto.response.OriginResponseDto;
import com.candidateSearch.searching.mapper.IMapperOrigin;
import com.candidateSearch.searching.repository.IOriginRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OriginService {

    private final IOriginRepository originRepository;
    private final ICandidateRepository candidateRepository;
    private final IRoleRepository roleRepository;
    private final IMapperOrigin mapperOrigin;

    public OriginResponseDto getOriginById(Long id){
        return originRepository.findById(id)
                .map(mapperOrigin::toDto)
                .orElseThrow(()-> new CustomNotFoundException("There is no origin with that ID."));
    }

    public List<OriginResponseDto> getAllOrigin(){
        return originRepository.findAll().stream()
                .map(mapperOrigin::toDto)
                .collect(Collectors.toList());
    }

    public OriginResponseDto saveOrigin(OriginRequestDto originRequestDto) {
        if(originRepository.existsByName(originRequestDto.getName())){
            throw new CustomConflictException("There is already an origin with that name.");
        }

        OriginEntity originEntity = mapperOrigin.toEntity(originRequestDto);
        OriginEntity originEntitySave = originRepository.save(originEntity);

        return mapperOrigin.toDto(originEntitySave);
    }

    public OriginResponseDto updateOrigin(Long id, OriginRequestDto originRequestDto) {
        OriginEntity existingOrigin = originRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no origin with that ID."));

        existingOrigin.setName(originRequestDto.getName());
        OriginEntity updatedOrigin = originRepository.save(existingOrigin);

        return mapperOrigin.toDto(updatedOrigin);
    }

    @Transactional
    public void deleteOrigin(Long id){
        OriginEntity existingOrigin = originRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no origin with that ID."));

        List<CandidateEntity> candidates = existingOrigin.getCandidates();
        if (candidates != null && !candidates.isEmpty()) {
            for (CandidateEntity candidate : candidates) {
                candidate.setOrigin(null);
                candidateRepository.save(candidate);
            }
        }

        List<RoleEntity> rolesExist = roleRepository.findAllByOriginId(id);
        if(rolesExist != null && !rolesExist.isEmpty()){
            for (RoleEntity role : rolesExist){
                role.setOrigin(null);
                roleRepository.save(role);
            }
        }

        originRepository.delete(existingOrigin);
    }
}
