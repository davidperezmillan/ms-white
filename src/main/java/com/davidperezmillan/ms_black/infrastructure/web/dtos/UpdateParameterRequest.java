package com.davidperezmillan.ms_black.infrastructure.web.dtos;

import lombok.Data;

@Data
public class UpdateParameterRequest {

    private String type;
    private String key;
    private String value;
}
