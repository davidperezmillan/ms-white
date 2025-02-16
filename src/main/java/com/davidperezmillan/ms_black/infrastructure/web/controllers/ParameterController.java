package com.davidperezmillan.ms_black.infrastructure.web.controllers;

import com.davidperezmillan.ms_black.applicacion.usecase.GetParameterUseCase;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ResponseGeneric;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.UpdateParameterRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/parameter")
public class ParameterController {

    private final GetParameterUseCase getParameterUseCase;

    public ParameterController(GetParameterUseCase getParameterUseCase) {
        this.getParameterUseCase = getParameterUseCase;
    }

    @GetMapping("/{type}/{key}")
    public ResponseEntity<ResponseGeneric<String>> getParameter(@PathVariable String type,
                                                                @PathVariable String key) {
        Optional<String> parameter = Optional.ofNullable(getParameterUseCase.getParameter(type, key));
        ResponseGeneric<String> response = new ResponseGeneric<>(1, parameter.orElse(null));
        if (parameter.isEmpty()) {
            response.setCount(0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<ResponseGeneric<Object>> updateParameter(@RequestBody UpdateParameterRequest request) {
        boolean updated = getParameterUseCase.updateParameter(request.getType(), request.getKey(), request.getValue());
        ResponseGeneric<Object> response = new ResponseGeneric<>(1, null);
        if (!updated) {
            response.setCount(0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
