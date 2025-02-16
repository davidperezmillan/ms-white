package com.davidperezmillan.ms_black.infrastructure.external.qr.services;

import com.davidperezmillan.ms_black.applicacion.ports.QRCodePort;
import com.davidperezmillan.ms_black.domain.model.QRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService implements QRCodePort {

    public void generateQRCode(QRCode qrCode) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(qrCode.getUrl(), BarcodeFormat.QR_CODE, qrCode.getWidth(), qrCode.getHeight(), hints);

        Path path = FileSystems.getDefault().getPath(qrCode.getFilePath());
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
