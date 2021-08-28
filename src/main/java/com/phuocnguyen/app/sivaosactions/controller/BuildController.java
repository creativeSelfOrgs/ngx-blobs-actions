package com.phuocnguyen.app.sivaosactions.controller;

import com.phuocnguyen.app.sivaosactions.service.BuildService;
import com.sivaos.Model.Request.BuildRequestDTO;
import com.sivaos.Model.Response.SIVAResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sivaos.Variables.EndPointVariable.ENDPOINT_BUILD_STRUCTURES;
import static com.sivaos.Variables.EndPointVariable.ENDPOINT_BUILD_URL;

@RestController
@RequestMapping(value = ENDPOINT_BUILD_URL)
public class BuildController {

    private final BuildService buildService;

    @Autowired
    public BuildController(BuildService buildService) {
        this.buildService = buildService;
    }

    @PostMapping(ENDPOINT_BUILD_STRUCTURES)
    public @ResponseBody
    ResponseEntity<SIVAResponseDTO<?>> buildStructuresProject(@RequestBody BuildRequestDTO buildRequestDTO) {
        return new ResponseEntity<>(buildService.buildAsyncStructureProject(buildRequestDTO), HttpStatus.OK);
    }
}
