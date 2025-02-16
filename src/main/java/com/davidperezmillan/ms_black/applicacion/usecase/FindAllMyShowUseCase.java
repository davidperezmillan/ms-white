package com.davidperezmillan.ms_black.applicacion.usecase;

import com.davidperezmillan.ms_black.infrastructure.web.dtos.ShowResponse;

import java.util.List;

public interface FindAllMyShowUseCase {

    int findAllMyShow(int contador);

    List<ShowResponse> allModels();

    List<ShowResponse> allModels(String tag);

    ShowResponse getModelById(int showId);

    boolean deleteModel(long id);

    ShowResponse randomModel();

    // search show by tag and title
    List<ShowResponse> searchShow(String search);
}
