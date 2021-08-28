package com.phuocnguyen.app.sivaosactions.controller;

import com.phuocnguyen.app.sivaosactions.service.ClonesService;
import com.sivaos.Model.Response.SIVAResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.sivaos.Variables.EndPointVariable.*;

@RestController
@RequestMapping(value = ENDPOINT_ACTIONS_URL)
public class ClonesController {

    private final ClonesService clonesService;

    @Autowired
    public ClonesController(ClonesService clonesService) {
        this.clonesService = clonesService;
    }

    @GetMapping(ENDPOINT_ACTIONS_DOWNLOAD_LANDING_PAGES)
    public SIVAResponseDTO<?> snagAsClones(@RequestParam("file") MultipartFile file,
                                           @RequestParam("nameSheet") String nameSheet) throws IOException {

        return clonesService.snagClones(file.getInputStream(), nameSheet);
    }

    @GetMapping(ENDPOINT_ACTIONS_DOWNLOAD_IMAGES_INS)
    public @ResponseBody
    ResponseEntity<?> snagIns(@RequestParam("file") MultipartFile file,
                              @RequestParam("nameSheet") String nameSheet) throws IOException {
        return new ResponseEntity<>(clonesService.snagIns(file.getInputStream(), nameSheet), HttpStatus.OK);
    }
}
