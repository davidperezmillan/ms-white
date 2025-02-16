package com.davidperezmillan.ms_black.applicacion.services;

import com.davidperezmillan.ms_black.applicacion.ports.QRCodePort;
import com.davidperezmillan.ms_black.applicacion.usecase.GenerateQRCaseUse;
import com.davidperezmillan.ms_black.domain.model.QRCode;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@Slf4j
public class GenerateQRService implements GenerateQRCaseUse {

    private final QRCodePort qrCodePort;

    @Autowired
    public GenerateQRService(QRCodePort qrCodePort) {
        this.qrCodePort = qrCodePort;
    }


    @Override
    public void generateQR(QRCode qrCode)  {
        try {
            qrCodePort.generateQRCode(qrCode);
        } catch (IOException e) {
            log.error("Error al generar el fichero QR", e);
            throw new RuntimeException(e);
        } catch (WriterException e) {
            log.error("Error al generar QR", e);
            throw new RuntimeException(e);
        }

    }
}
