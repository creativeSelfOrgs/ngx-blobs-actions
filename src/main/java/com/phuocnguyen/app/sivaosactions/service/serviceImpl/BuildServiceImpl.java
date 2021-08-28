package com.phuocnguyen.app.sivaosactions.service.serviceImpl;

import com.phuocnguyen.app.sivaosactions.service.BuildService;
import com.sivaos.Model.Request.BuildRequestDTO;
import com.sivaos.Model.Response.SIVAResponseDTO;
import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSProjectServiceImplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "buildService")
public class BuildServiceImpl implements BuildService {

    private static final Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);

    SIVAOSProjectServiceImplement sivaosProjectServiceImplement;

    @Override
    public SIVAResponseDTO<?> buildAsyncStructureProject(BuildRequestDTO buildRequestDTO) {
        sivaosProjectServiceImplement = new SIVAOSProjectServiceImplement();
        logger.info("body current build spring boot: {}", buildRequestDTO.toString());
        return sivaosProjectServiceImplement.buildAsyncStructureProject(buildRequestDTO);
    }
}
