package com.davidperezmillan.ms_black.applicacion.ports;

import com.davidperezmillan.ms_black.domain.model.QRCode;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRCodePort {

        void generateQRCode(QRCode qrCode) throws WriterException, IOException;
}
