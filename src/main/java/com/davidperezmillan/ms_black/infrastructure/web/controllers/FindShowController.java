package com.davidperezmillan.ms_black.infrastructure.web.controllers;

import com.davidperezmillan.ms_black.applicacion.usecase.FindAllMyShowUseCase;
import com.davidperezmillan.ms_black.infrastructure.bbdd.ParameterConstants;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Parameter;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.ParameterService;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ResponseGeneric;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ShowResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/show")
public class FindShowController {

    private final FindAllMyShowUseCase findAllMyShowUseCase;
    private final ParameterService parameterService;

    @Autowired
    public FindShowController(FindAllMyShowUseCase findAllMyShowUseCase, ParameterService parameterService) {
        this.findAllMyShowUseCase = findAllMyShowUseCase;

        this.parameterService = parameterService;
    }

    @GetMapping("/find")
    public ResponseEntity<ResponseGeneric<Object>> findAll() {
        int count = findAllMyShowUseCase.findAllMyShow(parameterService.getParameter(ParameterConstants.TYPE_SHOW, ParameterConstants.SHOWS_PAGE_FIND, 5));
        ResponseGeneric<Object> response = new ResponseGeneric<>(count, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<ResponseGeneric<Object>> findById(@PathVariable int showId) {
        log.info("Find all models by id: {}", showId);
        ShowResponse showsResponse = findAllMyShowUseCase.getModelById(showId);
        ResponseGeneric<Object> response = new ResponseGeneric<>(1, showsResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/models")
    public ResponseEntity<ResponseGeneric<Object>> findAllModels() {
        List<ShowResponse> showsResponse = findAllMyShowUseCase.allModels();
        ResponseGeneric<Object> response = new ResponseGeneric<>(showsResponse.size(), showsResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/models/{tag}")
    public ResponseEntity<ResponseGeneric<Object>> findAllModelsByTag(@PathVariable String tag) {
        log.info("Find all models by tag: {}", tag);
        List<ShowResponse> showsResponse = findAllMyShowUseCase.allModels(tag);
        ResponseGeneric<Object> response = new ResponseGeneric<>(showsResponse.size(), showsResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @GetMapping("/models/random")
    public ResponseEntity<ResponseGeneric<Object>> findRandomModel() {
        ShowResponse showResponse = findAllMyShowUseCase.randomModel();
        ResponseGeneric<Object> response = new ResponseGeneric<>(0, null);
        if (showResponse == null) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.setCount(1);
        response.setContent(showResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<ResponseGeneric<Object>> searchShow(@PathVariable String search) {
        log.info("Search show by tag or title: {}", search);
        List<ShowResponse> showsResponse = findAllMyShowUseCase.searchShow(search);
        ResponseGeneric<Object> response = new ResponseGeneric<>(showsResponse.size(), showsResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/models/{id}")
    public ResponseEntity<ResponseGeneric<Object>> deleteModel(@PathVariable long id) {
        log.info("Delete model with id: {}", id);
        boolean deleted = findAllMyShowUseCase.deleteModel(id);
        if (!deleted) {
            ResponseGeneric<Object> response = new ResponseGeneric<>(0, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        ResponseGeneric<Object> response = new ResponseGeneric<>(1, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}