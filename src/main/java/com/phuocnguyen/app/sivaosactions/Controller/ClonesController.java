package com.phuocnguyen.app.sivaosactions.Controller;

import com.phuocnguyen.app.sivaosactions.Service.ClonesService;
import com.sivaos.Model.Response.SIVAResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.sivaos.Variables.EndPointVariable.ENDPOINT_ACTIONS_DOWNLOAD_LANDING_PAGES;
import static com.sivaos.Variables.EndPointVariable.ENDPOINT_ACTIONS_URL;

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
}
