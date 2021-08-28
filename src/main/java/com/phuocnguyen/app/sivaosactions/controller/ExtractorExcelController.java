package com.phuocnguyen.app.sivaosactions.controller;

import com.phuocnguyen.app.sivaosactions.service.ExtractorExcelService;
import com.sivaos.Model.ExtractorExcelDTO;
import com.sivaos.Model.Response.Extend.HttpStatusCodesResponseDTO;
import com.sivaos.Model.Response.Extend.StatusCodeResponseDTO;
import com.sivaos.Model.Response.SIVAResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.sivaos.Variables.EndPointVariable.ENDPOINT_EXTRACTOR_EXCEL_URL;


@RestController
@RequestMapping(value = ENDPOINT_EXTRACTOR_EXCEL_URL)
public class ExtractorExcelController {

    private final ExtractorExcelService extractorExcelService;

    @Autowired
    public ExtractorExcelController(ExtractorExcelService extractorExcelService) {
        this.extractorExcelService = extractorExcelService;
    }

    @PostMapping("/new/{isIncludeHeader}/{typeDataOutput}/{owner}")
    public @ResponseBody
    ResponseEntity<?> extractorExcel(@PathVariable(value = "isIncludeHeader") boolean isIncludeHeader,
                                     @PathVariable(value = "typeDataOutput") String typeDataOutput,
                                     @PathVariable(value = "owner") String owner,
                                     @RequestParam("file") MultipartFile file,
                                     @RequestParam("sheetName") String sheetName,
                                     @RequestParam("columnName") String columnName) {
        StatusCodeResponseDTO statusCodeResponseDTO1 = HttpStatusCodesResponseDTO.OK;
        ExtractorExcelDTO extractorExcelDTO = extractorExcelService.extractorExcel(isIncludeHeader, typeDataOutput, owner, file, sheetName, columnName);
        return new ResponseEntity<>(SIVAResponseDTO.buildSIVAResponse(extractorExcelDTO, statusCodeResponseDTO1), HttpStatus.OK);
    }
}
