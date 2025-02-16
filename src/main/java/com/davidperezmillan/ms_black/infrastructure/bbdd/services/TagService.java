package com.davidperezmillan.ms_black.infrastructure.bbdd.services;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Tag;
import com.davidperezmillan.ms_black.infrastructure.bbdd.repositories.ShowRepository;
import com.davidperezmillan.ms_black.infrastructure.bbdd.repositories.TagRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TagService {


    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    /** TAGS **/
    public void save(Tag tag){
        try{
            tagRepository.findByName(tag.getName()).ifPresentOrElse(
                    t -> log.warn("Tag already exists: {}", t.getName()),
                    () -> tagRepository.save(tag)
            );
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving duplicate tag: {} - {}", e.getMessage(), tag.getName());
        } catch (Exception e) {
            log.error("Error saving tag: {}", e.getMessage());
        }
    }

    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }
    public Optional<List<Tag>> findAllTags() {
        return Optional.of(tagRepository.findAll());
    }
}
