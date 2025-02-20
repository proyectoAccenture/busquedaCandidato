package com.busquedaCandidato.candidato.controller;


import com.busquedaCandidato.candidato.dto.response.AddStateResponseDto;
import com.busquedaCandidato.candidato.service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/state")
@RequiredArgsConstructor
public class StateController {

    @Autowired
    private  StateService stateService;

    @GetMapping("/{id}")
    public ResponseEntity<AddStateResponseDto> getState(@PathVariable Long id){

        return null;
    }

}
