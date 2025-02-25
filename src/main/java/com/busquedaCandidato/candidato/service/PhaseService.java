package com.busquedaCandidato.candidato.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busquedaCandidato.candidato.repository.IPhaseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PhaseService {

    @Autowired
    private final IPhaseRepository phaseRepository;


}
