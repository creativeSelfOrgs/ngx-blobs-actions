package com.phuocnguyen.app.sivaosactions.service;

import com.sivaos.Model.ExtractorExcelDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ExtractorExcelService {

    ExtractorExcelDTO extractorExcel(boolean isIncludeHeader, String typeDataOutput, String owner, MultipartFile file, String sheetName, String columnName);
}
