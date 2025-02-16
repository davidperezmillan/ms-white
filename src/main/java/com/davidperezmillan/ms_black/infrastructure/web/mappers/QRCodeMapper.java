package com.davidperezmillan.ms_black.infrastructure.web.mappers;

import com.davidperezmillan.ms_black.domain.model.QRCode;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.QRCodeRequest;
import org.modelmapper.ModelMapper;

public interface QRCodeMapper {

    static QRCode map(QRCodeRequest qrCodeRequest) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(qrCodeRequest, QRCode.class);
    }
}
