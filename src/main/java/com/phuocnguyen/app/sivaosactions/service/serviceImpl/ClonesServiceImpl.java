package com.phuocnguyen.app.sivaosactions.service.serviceImpl;

import com.phuocnguyen.app.sivaosactions.model.request.ClonesRequestDTO;
import com.phuocnguyen.app.sivaosactions.service.ClonesService;
import com.sivaos.Model.MessageResponseDTO;
import com.sivaos.Model.Response.Extend.HttpStatusCodesResponseDTO;
import com.sivaos.Model.Response.Extend.StatusCodeResponseDTO;
import com.sivaos.Model.Response.SIVAResponseDTO;
import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSHttpRequestServiceImplement;
import com.sivaos.Utils.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service(value = "clonesService")
public class ClonesServiceImpl implements ClonesService {

    private static final Logger logger = LoggerFactory.getLogger(ClonesServiceImpl.class);
    private static SIVAOSHttpRequestServiceImplement sivaosHttpRequestServiceImplement = new SIVAOSHttpRequestServiceImplement();

    @Value("${sivaos-support.link-clone-LDP}")
    private String linkCloneLDP;
    @Value("${sivaos-support.link-clone-images-ins}")
    private String linkCloneImagesIns;
    @Value("${sivaos-support.link-query-completed-ins}")
    private String linkQueryAsCompletedIns;

    @Override
    public SIVAResponseDTO<?> snagClones(InputStream inputStream, String nameSheet) {
        String hostname = NetworkingUtils.snagPublicIPAsUrl(linkCloneLDP, 1);
        URL url = NetworkingUtils.establishUrlConnection(linkCloneLDP);
        sivaosHttpRequestServiceImplement.makeHOSTConnection(hostname, url.getPort(), url.getProtocol());
        List<String> response = new ArrayList<>();
        List<String> responseAsFailure = new ArrayList<>();
        List<String> resultCounts = new ArrayList<>();
        List<ClonesRequestDTO> clonesRequestDTOS = new ArrayList<>();
        List<ClonesRequestDTO> clonesRequestInvalidDTOS = new ArrayList<>();
        StatusCodeResponseDTO statusCodeResponseDTO1 = HttpStatusCodesResponseDTO.BAD_REQUEST;
        StatusCodeResponseDTO statusCodeResponseDTO2 = HttpStatusCodesResponseDTO.OK;
        MessageResponseDTO messageResponseDTO;
        int countAsSuccess = 0;
        int countAsFailure = 0;
        if (ObjectUtils.isEmpty(nameSheet)) {
            return SIVAResponseDTO.buildSIVAResponse(null, statusCodeResponseDTO1);
        }
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(nameSheet);
            Iterator<Row> rowIterator = sheet.iterator();
            List<ClonesRequestDTO> clonesRequestDTOList = new ArrayList<>();
            Map<String, String> params = new HashMap<>();
            int row = 0;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                if (row == 0) { /* ignore header*/
                    row++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                ClonesRequestDTO clonesRequestDTO = new ClonesRequestDTO();
                int cell = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cell) {
                        case 0:
                            clonesRequestDTO.setUrl(currentCell.getStringCellValue());
                            break;
                        case 1:
                            clonesRequestDTO.setSourceAsDirs(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cell++;
                }
                clonesRequestDTOList.add(clonesRequestDTO);
            }

            if (ObjectUtils.isEmpty(clonesRequestDTOList)) {
                return SIVAResponseDTO.buildSIVAResponse(null, statusCodeResponseDTO1);
            }

            for (ClonesRequestDTO requestDTO : clonesRequestDTOList) {

                if (ObjectUtils.allNotNull(requestDTO.getUrl(), requestDTO.getSourceAsDirs()) && !ObjectUtils.isEmpty(requestDTO.getUrl())) {
                    if (FileUtils.isFileExist(requestDTO.getSourceAsDirs())) {
                        FileUtils.snagAsDeleteSource(requestDTO.getSourceAsDirs());
                    }

                    if (ValidationUtils.isVerifiedAsUrl(requestDTO.getUrl())) {
                        if (!requestDTO.getUrl().startsWith("https://")) {
                            requestDTO.setUrl(requestDTO.getUrl().replace("http://", "https://"));
                        }
                        if (NetworkingUtils.snagAsRealConnection(requestDTO.getUrl())) {
                            clonesRequestDTOS.add(requestDTO);
                            countAsSuccess++;
                        }

                    } else {
                        countAsFailure++;
                        clonesRequestInvalidDTOS.add(requestDTO);
                    }
                }

            }

            for (ClonesRequestDTO requestDTO : clonesRequestInvalidDTOS) {
                if (!ObjectUtils.isEmpty(requestDTO.getUrl())) {
                    responseAsFailure.add(requestDTO.getUrl().concat(" -> ").concat("failure 400"));
                }
            }

            for (ClonesRequestDTO requestDTO : clonesRequestDTOS) {
                params.put("link", requestDTO.getUrl());
                params.put("resource", requestDTO.getSourceAsDirs());
                messageResponseDTO = sivaosHttpRequestServiceImplement.makeGETRequest(linkCloneLDP, UrlQueryUtils.snagQuery(params));
                response.add(requestDTO.getUrl().concat(" -> ").concat(messageResponseDTO.getMessage()) + " " + messageResponseDTO.getStatusCodeResponseDTO().getCode());
            }

            resultCounts.add("no. link success: ".concat(ExchangeUtils.exchangeIntegerToStringUsingIntegerToString(countAsSuccess)));
            resultCounts.add("no. link failure: ".concat(ExchangeUtils.exchangeIntegerToStringUsingIntegerToString(countAsFailure)));

            if (CollectionUtils.isEmpty(response)) {
                if (ObjectUtils.isEmpty(responseAsFailure)) {
                    return SIVAResponseDTO.buildSIVAResponse(resultCounts, statusCodeResponseDTO2);
                }
                return SIVAResponseDTO.buildSIVAResponse(EdifyUtils.merge(resultCounts, responseAsFailure), statusCodeResponseDTO2);
            }
            return SIVAResponseDTO.buildSIVAResponse(EdifyUtils.merge(response, EdifyUtils.merge(resultCounts, responseAsFailure)), statusCodeResponseDTO2);

        } catch (IOException message) {
            logger.error(message.getMessage(), message);
        }
        return SIVAResponseDTO.buildSIVAResponse(null, statusCodeResponseDTO1);
    }

    @Override
    public SIVAResponseDTO<?> snagIns(InputStream inputStream, String nameSheet) {
        String hostname = NetworkingUtils.snagPublicIPAsUrl(linkCloneImagesIns, 1);
        URL url = NetworkingUtils.establishUrlConnection(linkCloneImagesIns);
        sivaosHttpRequestServiceImplement.makeHOSTConnection(hostname, url.getPort(), url.getProtocol());
        List<String> response = new ArrayList<>();
        List<String> responseAsFailure = new ArrayList<>();
        List<String> resultCounts = new ArrayList<>();
        List<ClonesRequestDTO> clonesRequestDTOS = new ArrayList<>();
        List<ClonesRequestDTO> clonesRequestInvalidDTOS = new ArrayList<>();
        StatusCodeResponseDTO statusCodeResponseDTO1 = HttpStatusCodesResponseDTO.BAD_REQUEST;
        StatusCodeResponseDTO statusCodeResponseDTO2 = HttpStatusCodesResponseDTO.OK;
        MessageResponseDTO messageResponseDTO;
        int countAsSuccess = 0;
        int countAsFailure = 0;
        if (ObjectUtils.isEmpty(nameSheet)) {
            return SIVAResponseDTO.buildSIVAResponse(null, statusCodeResponseDTO1);
        }
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(nameSheet);
            Iterator<Row> rowIterator = sheet.iterator();
            List<ClonesRequestDTO> clonesRequestDTOList = new ArrayList<>();
            Map<String, String> params = new HashMap<>();
            int row = 0;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                if (row == 0) { /* ignore header*/
                    row++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                ClonesRequestDTO clonesRequestDTO = new ClonesRequestDTO();
                int cell = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cell) {
                        case 0:
                            clonesRequestDTO.setUrl(currentCell.getStringCellValue());
                            break;
                        case 1:
                            clonesRequestDTO.setSourceAsDirs(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cell++;
                }
                clonesRequestDTOList.add(clonesRequestDTO);
            }

            if (ObjectUtils.isEmpty(clonesRequestDTOList)) {
                return SIVAResponseDTO.buildSIVAResponse(null, statusCodeResponseDTO1);
            }

            for (ClonesRequestDTO requestDTO : clonesRequestDTOList) {

                if (ObjectUtils.allNotNull(requestDTO.getUrl(), requestDTO.getSourceAsDirs()) && !ObjectUtils.isEmpty(requestDTO.getUrl())) {
                    if (FileUtils.isFileExist(requestDTO.getSourceAsDirs())) {
                        FileUtils.snagAsDeleteSource(requestDTO.getSourceAsDirs());
                    }

                    if (ValidationUtils.isVerifiedAsUrl(requestDTO.getUrl())) {
                        clonesRequestDTOS.add(requestDTO);
                        countAsSuccess++;
                    } else {
                        countAsFailure++;
                        clonesRequestInvalidDTOS.add(requestDTO);
                    }
                }

            }


            for (ClonesRequestDTO requestDTO : clonesRequestDTOS) {
                params.put("url", requestDTO.getUrl());
                params.put("path", requestDTO.getSourceAsDirs());
                messageResponseDTO = sivaosHttpRequestServiceImplement.makeGETRequest(linkCloneImagesIns, UrlQueryUtils.snagQuery(params));
                response.add(requestDTO.getUrl().concat(" -> ").concat(messageResponseDTO.getMessage()) + " " + messageResponseDTO.getStatusCodeResponseDTO().getCode());
            }

            if (!ObjectUtils.isEmpty(clonesRequestInvalidDTOS)) {
                for (ClonesRequestDTO requestDTO : clonesRequestInvalidDTOS) {
                    if (!ObjectUtils.isEmpty(requestDTO.getUrl())) {
                        params.put("url", linkQueryAsCompletedIns.concat(requestDTO.getUrl()));
                        params.put("path", requestDTO.getSourceAsDirs());
                        messageResponseDTO = sivaosHttpRequestServiceImplement.makeGETRequest(linkCloneImagesIns, UrlQueryUtils.snagQuery(params));
                        responseAsFailure.add(requestDTO.getUrl().concat(" -> ").concat(messageResponseDTO.getMessage()) + " " + messageResponseDTO.getStatusCodeResponseDTO().getCode());
                    }
                }
            }

            resultCounts.add("no. link success: ".concat(ExchangeUtils.exchangeIntegerToStringUsingIntegerToString(countAsSuccess)));
            resultCounts.add("no. link failure, request again {success: 200}: ".concat(ExchangeUtils.exchangeIntegerToStringUsingIntegerToString(countAsFailure)));

            if (CollectionUtils.isEmpty(response)) {
                if (ObjectUtils.isEmpty(responseAsFailure)) {
                    return SIVAResponseDTO.buildSIVAResponse(resultCounts, statusCodeResponseDTO2);
                }
                return SIVAResponseDTO.buildSIVAResponse(EdifyUtils.merge(resultCounts, responseAsFailure), statusCodeResponseDTO2);
            }
            return SIVAResponseDTO.buildSIVAResponse(EdifyUtils.merge(response, EdifyUtils.merge(resultCounts, responseAsFailure)), statusCodeResponseDTO2);

        } catch (IOException message) {
            logger.error(message.getMessage(), message);
        }
        return SIVAResponseDTO.buildSIVAResponse(null, statusCodeResponseDTO1);
    }
}
