package com.phuocnguyen.app.sivaosactions.service;

import com.sivaos.Model.Request.BuildRequestDTO;
import com.sivaos.Model.Response.SIVAResponseDTO;

public interface BuildService {

    SIVAResponseDTO<?> buildAsyncStructureProject(BuildRequestDTO buildRequestDTO);
}
