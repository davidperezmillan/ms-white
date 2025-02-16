package com.davidperezmillan.ms_black.applicacion.services;

import com.davidperezmillan.ms_black.applicacion.ports.QRCodePort;
import com.davidperezmillan.ms_black.applicacion.usecase.GenerateQRCaseUse;
import com.davidperezmillan.ms_black.applicacion.usecase.GetParameterUseCase;
import com.davidperezmillan.ms_black.domain.model.QRCode;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Parameter;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.ParameterService;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
@Slf4j
public class GetParameterService implements GetParameterUseCase {

    private final ParameterService parameterService;


    public GetParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @Override
    public String getParameter(String type, String key) {
        return parameterService.getParameter(type.toUpperCase(), key.toUpperCase());
    }

    @Override
    public boolean updateParameter(String type, String key, String value) {
        return parameterService.updateParameter(type.toUpperCase(), key.toUpperCase(), value.toUpperCase());
    }
}
