package com.davidperezmillan.ms_black.applicacion.usecase;

public interface GetParameterUseCase {

    String getParameter(String type, String key);

    boolean updateParameter(String type, String key, String value);
}
