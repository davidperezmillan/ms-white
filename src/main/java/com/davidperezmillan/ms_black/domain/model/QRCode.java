package com.davidperezmillan.ms_black.domain.model;

import lombok.Data;

@Data
public class QRCode {

    private String url;
    private String filePath;
    private int width;
    private int height;

}
