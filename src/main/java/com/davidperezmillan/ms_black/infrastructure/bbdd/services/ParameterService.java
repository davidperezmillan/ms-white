package com.davidperezmillan.ms_black.infrastructure.bbdd.services;

import com.davidperezmillan.ms_black.infrastructure.bbdd.ParameterConstants;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Parameter;
import com.davidperezmillan.ms_black.infrastructure.bbdd.repositories.ParameterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class ParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    public String getSheduledTime() {
        Optional<Parameter> parameter = parameterRepository.findByTypeAndKey(ParameterConstants.TYPE_SHOW, ParameterConstants.CRON_EXPRESSION);
        return (String) parameter.map(Parameter::getValue).orElse("0 0 */6 * * *");
    }

    public String getParameter(String type, String key) {
        Optional<Parameter> parameter = parameterRepository.findByTypeAndKey(type, key);
        return (String) parameter.map(Parameter::getValue).orElse(null);
    }

    public boolean getParameter(String type, String key, boolean defaultValue) {
        Optional<Parameter> parameter = parameterRepository.findByTypeAndKey(type, key);
        return parameter.map(p -> Boolean.parseBoolean(p.getValue())).orElse(defaultValue);
    }

    public int getParameter(String type, String key, int defaultValue) {
        Optional<Parameter> parameter = parameterRepository.findByTypeAndKey(type, key);
        return parameter.map(p -> Integer.parseInt(p.getValue())).orElse(defaultValue);

    }

    public boolean updateParameter(String type, String key, String value) {
        Optional<Parameter> parameter = parameterRepository.findByTypeAndKey(type, key);
        if (parameter.isPresent()) {
            parameter.get().setValue(value);
            parameterRepository.save(parameter.get());
            return true;
        }
        return false;
    }
}