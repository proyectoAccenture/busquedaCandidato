package com.candidateSearch.searching.service;

import java.util.List;
import java.util.stream.Collectors;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import org.springframework.stereotype.Service;
import com.candidateSearch.searching.dto.request.OriginRequestDto;
import com.candidateSearch.searching.dto.response.OriginResponseDto;
import com.candidateSearch.searching.exception.type.EntityAlreadyExistsException;
import com.candidateSearch.searching.mapper.IMapperOrigin;
import com.candidateSearch.searching.repository.IOriginRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OriginService {

    private final IOriginRepository originRepository;
    private final IMapperOrigin mapperOrigin;

    public OriginResponseDto getOriginById(Long id){
        return originRepository.findById(id)
                .map(mapperOrigin::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<OriginResponseDto> getAllOrigin(){
        return originRepository.findAll().stream()
                .map(mapperOrigin::toDto)
                .collect(Collectors.toList());
    }

    public OriginResponseDto saveOrigin(OriginRequestDto originRequestDto) {
        if(originRepository.existsByName(originRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }

        OriginEntity originEntity = mapperOrigin.toEntity(originRequestDto);
        OriginEntity originEntitySave = originRepository.save(originEntity);

        return mapperOrigin.toDto(originEntitySave);
    }

    public OriginResponseDto updateOrigin(Long id, OriginRequestDto originRequestDto) {
        OriginEntity existingOrigin = originRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingOrigin.setName(originRequestDto.getName());
        OriginEntity updatedOrigin = originRepository.save(existingOrigin);

        return mapperOrigin.toDto(updatedOrigin);
    }

    public void deleteOrigin(Long id){
        OriginEntity existingOrigin = originRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        originRepository.delete(existingOrigin);
    }
}
