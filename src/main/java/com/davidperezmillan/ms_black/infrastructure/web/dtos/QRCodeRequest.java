package com.davidperezmillan.ms_black.infrastructure.web.dtos;

import lombok.Data;

@Data
public class QRCodeRequest {
    private String url;
    private String filePath;
    private int width;
    private int height;

}
