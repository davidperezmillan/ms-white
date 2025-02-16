package com.davidperezmillan.ms_black.infrastructure.web.controllers;

import com.davidperezmillan.ms_black.applicacion.usecase.GenerateQRCaseUse;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.QRCodeRequest;
import com.davidperezmillan.ms_black.infrastructure.web.mappers.QRCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
public class QRCodeController {

    @Autowired
    private final GenerateQRCaseUse generateQRCaseUse;

    @Autowired
    public QRCodeController(GenerateQRCaseUse generateQRCaseUse) {
        this.generateQRCaseUse = generateQRCaseUse;
    }


    @GetMapping("/generate-qr")
    @Deprecated
    public ResponseEntity<byte[]> generateQR(@RequestParam("url") String url) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Path filePath = Paths.get("temp_qr_code.png");
        QRCodeRequest qrCodeRequest = new QRCodeRequest();
        qrCodeRequest.setUrl(url);
        qrCodeRequest.setFilePath(filePath.toString());

        generateQRCaseUse.generateQR(QRCodeMapper.map(qrCodeRequest));

        stream.write(Files.readAllBytes(filePath));
        stream.flush();

        byte[] qrImage = stream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrImage.length);
        headers.set("Content-Disposition", "inline; filename=qr_code.png");

        return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
    }

    @PostMapping("/generate-qr")
    public ResponseEntity<byte[]> generateQRPost(@RequestBody QRCodeRequest qrCodeRequest) throws IOException {
        log.info("Objeto recibido : {}", qrCodeRequest);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Definimos el path dentro de la carpeta resources
        Path filePath = Paths.get(qrCodeRequest.getFilePath());
        qrCodeRequest.setFilePath(filePath.toString());

        generateQRCaseUse.generateQR(QRCodeMapper.map(qrCodeRequest));

        stream.write(Files.readAllBytes(filePath));
        stream.flush();

        byte[] qrImage = stream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrImage.length);
        headers.set("Content-Disposition", "inline; filename=qr_code.png");

        return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
    }
}
