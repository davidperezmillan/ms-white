package com.davidperezmillan.ms_black.infrastructure.bbdd.services;

import com.davidperezmillan.ms_black.infrastructure.bbdd.ParameterConstants;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Tag;
import com.davidperezmillan.ms_black.infrastructure.bbdd.repositories.ShowRepository;
import com.davidperezmillan.ms_black.infrastructure.bbdd.repositories.TagRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ShowService {
    
    private final ShowRepository showRepository;
    private final ParameterService parameterService;
    
    public ShowService(ShowRepository showRepository, ParameterService parameterService) {
        this.showRepository = showRepository;
        this.parameterService = parameterService;
    }

    public Optional<List<Show>> findAll() {
        log.warn("ShowService.findAll");
        return Optional.of(showRepository.findAll());
    }

    public Optional<List<Show>> findAllNotDelete() {
        return Optional.of(showRepository.findByDeletedFalse());
    }

    public Optional<List<Show>> findNDeleted() {
        return Optional.of(showRepository.findByDeletedFalse(getPageable()));
    }
    public Optional<List<Show>> findNDeletedByTag(String tag) {
        return Optional.of(showRepository.findByDeletedFalseAndTagsContaining(tag, getPageable()));
    }

    public void saveShows(List<Show> shows) {
        try {
            shows.forEach(show -> {
                if (!showRepository.findByShowId(show.getShowId()).isPresent()) {
                    showRepository.save(show);

                }
            });
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving duplicate shows: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error saving shows: {}", e.getMessage());
        }
    }

    public Optional<Show> findByShowId(String showId) {
        return showRepository.findByShowId(showId);
    }

    public void markAsDeleted(long id) {
        Optional<Show> show = showRepository.findById(id);
        show.ifPresent(value -> {
            value.setDeleted(true);
            showRepository.save(value);
        });
    }


    public Optional<Show> findById(long id) {
        return showRepository.findById(id);
    }


    // metodo que recupera de bbdd un show de forma aleatoria
    public Optional<Show> findRandomShow() {
        return showRepository.findRandomShow();
    }

    public Optional<List<Show>> findByTitleOrTagsContaining(String keyword) {
        return Optional.of(showRepository.findByTitleOrTagsContaining(keyword, getPageable()));
    }

    private Pageable getPageable() {
        int n = parameterService.getParameter(ParameterConstants.TYPE_SHOW, ParameterConstants.N_SHOWS, 200);
        return PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "id"));
    }
}
