package com.davidperezmillan.ms_black.infrastructure.external.qr.dtos;

import lombok.Data;

@Data
public class QRCodeGenerator {

    private String url;
    private String filePath;
    private int width;
    private int height;

}
