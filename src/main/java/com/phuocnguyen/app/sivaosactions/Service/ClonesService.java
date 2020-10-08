package com.phuocnguyen.app.sivaosactions.Service;

import com.sivaos.Model.Response.SIVAResponseDTO;

import java.io.InputStream;

public interface ClonesService {

    SIVAResponseDTO<?> snagClones(InputStream inputStream, String nameSheet);
}
