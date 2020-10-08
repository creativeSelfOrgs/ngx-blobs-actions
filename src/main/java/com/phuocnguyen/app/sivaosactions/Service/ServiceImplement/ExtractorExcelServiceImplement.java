package com.phuocnguyen.app.sivaosactions.Service.ServiceImplement;

import com.phuocnguyen.app.sivaosactions.Service.ExtractorExcelService;
import com.sivaos.Model.ExtractorExcelDTO;
import com.sivaos.Utils.LanguagesUtils;
import com.sivaos.Utils.SpreadSheetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ExtractorExcelServiceImplement implements ExtractorExcelService {

    private final static Logger logger = LoggerFactory.getLogger(ExtractorExcelServiceImplement.class);

    @Override
    public ExtractorExcelDTO extractorExcel(boolean isIncludeHeader, String typeDataOutput, String owner, MultipartFile file, String sheetName, String columnName) {
        try {
            SpreadSheetUtils.setSourceExcelFile(file.getInputStream());
            return ExtractorExcelDTO.buildExtractorExcelDTO(LanguagesUtils.stripAccents(file.getOriginalFilename()),
                    file.getContentType(),
                    file.getSize(),
                    SpreadSheetUtils.getRowsCount(sheetName),
                    SpreadSheetUtils.getColumnsCount(sheetName),
                    SpreadSheetUtils.getAllSheetName(),
                    SpreadSheetUtils.getNumberOfSheets(),
                    owner,
                    SpreadSheetUtils.getContentSpecificCellValueFromSheet(sheetName, columnName, isIncludeHeader, typeDataOutput));
        } catch (IOException error) {
            logger.error(error.getMessage(), error);
            return null;
        }


    }
}
