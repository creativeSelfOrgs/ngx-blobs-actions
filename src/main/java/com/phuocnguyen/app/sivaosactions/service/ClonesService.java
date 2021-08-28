package com.phuocnguyen.app.sivaosactions.service;

import com.sivaos.Model.Response.SIVAResponseDTO;

import java.io.InputStream;

public interface ClonesService {

    SIVAResponseDTO<?> snagClones(InputStream inputStream, String nameSheet);

    SIVAResponseDTO<?> snagIns(InputStream inputStream, String nameSheet);
}
