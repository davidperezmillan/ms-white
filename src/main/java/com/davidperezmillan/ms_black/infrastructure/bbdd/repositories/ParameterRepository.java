package com.davidperezmillan.ms_black.infrastructure.bbdd.repositories;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Parameter;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    Optional<Parameter> findByTypeAndKey(String type, String key);
}
