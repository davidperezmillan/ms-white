package com.davidperezmillan.ms_black.infrastructure.bbdd.repositories;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
}
