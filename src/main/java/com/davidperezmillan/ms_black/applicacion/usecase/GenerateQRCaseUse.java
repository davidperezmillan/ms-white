package com.davidperezmillan.ms_black.applicacion.usecase;

import com.davidperezmillan.ms_black.domain.model.QRCode;

import java.io.IOException;

public interface GenerateQRCaseUse {


    void generateQR(QRCode qrCode) throws IOException;
}
