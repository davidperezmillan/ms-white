package com.davidperezmillan.ms_black.infrastructure.web.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseGeneric<T> {

    private int count;
    private T content;


}
