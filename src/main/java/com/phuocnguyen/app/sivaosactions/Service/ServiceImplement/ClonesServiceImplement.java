package com.phuocnguyen.app.sivaosactions.Service.ServiceImplement;

import com.phuocnguyen.app.sivaosactions.Model.Request.ClonesRequestDTO;
import com.phuocnguyen.app.sivaosactions.Service.ClonesService;
import com.sivaos.Model.MessageResponseDTO;
import com.sivaos.Model.Response.Extend.HttpStatusCodesResponseDTO;
import com.sivaos.Model.Response.Extend.StatusCodeResponseDTO;
import com.sivaos.Model.Response.SIVAResponseDTO;
import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSHttpRequestServiceImplement;
import com.sivaos.Utils.NetworkingUtils;
import com.sivaos.Utils.ObjectUtils;
import com.sivaos.Utils.UrlQueryUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service(value = "clonesService")
public class ClonesServiceImplement implements ClonesService {

    private static final Logger logger = LoggerFactory.getLogger(ClonesServiceImplement.class);
    private static SIVAOSHttpRequestServiceImplement sivaosHttpRequestServiceImplement = new SIVAOSHttpRequestServiceImplement();

    @Value("${sivaos-support.link-clone-LDP}")
    private String linkCloneLDP;

    @Override
    public SIVAResponseDTO<?> snagClones(InputStream inputStream, String nameSheet) {
        List<String> response = new ArrayList<>();
        StatusCodeResponseDTO statusCodeResponseDTO1 = HttpStatusCodesResponseDTO.BAD_REQUEST;
        StatusCodeResponseDTO statusCodeResponseDTO2 = HttpStatusCodesResponseDTO.OK;
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

            String hostname = NetworkingUtils.snagPublicIPAsUrl(linkCloneLDP, 1);
            URL url = NetworkingUtils.establishUrlConnection(linkCloneLDP);
            sivaosHttpRequestServiceImplement.makeHOSTConnection(hostname, url.getPort(), url.getProtocol());
            for (ClonesRequestDTO requestDTO : clonesRequestDTOList) {
                params.put("link", requestDTO.getUrl());
                params.put("resource", requestDTO.getSourceAsDirs());
                MessageResponseDTO messageResponseDTO = sivaosHttpRequestServiceImplement.makeGETRequest(linkCloneLDP, UrlQueryUtils.snagQuery(params));
                response.add(requestDTO.getUrl().concat(" -> ").concat(messageResponseDTO.getMessage()) + " " + messageResponseDTO.getStatusCodeResponseDTO().getCode());
            }
            return SIVAResponseDTO.buildSIVAResponse(response, statusCodeResponseDTO2);

        } catch (IOException message) {
            logger.error(message.getMessage(), message);
        }
        return SIVAResponseDTO.buildSIVAResponse(null, statusCodeResponseDTO1);
    }
}
