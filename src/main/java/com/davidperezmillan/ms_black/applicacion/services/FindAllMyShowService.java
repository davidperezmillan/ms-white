package com.davidperezmillan.ms_black.applicacion.services;

import com.davidperezmillan.ms_black.applicacion.ports.ScrapShowPort;
import com.davidperezmillan.ms_black.applicacion.usecase.FindAllMyShowUseCase;
import com.davidperezmillan.ms_black.infrastructure.bbdd.mappers.ShowMapper;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.ShowService;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ShowResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FindAllMyShowService implements FindAllMyShowUseCase {

    private final ScrapShowPort scrapShowPort;
    private final ShowService showService;

    public FindAllMyShowService(ScrapShowPort scrapShowPort,
                                ShowService showService) {
        this.scrapShowPort = scrapShowPort;
        this.showService = showService;
    }


    @Override
    public int findAllMyShow(int contador) {
        return scrapShowPort.findAll(contador);
    }

    @Override
    public List<ShowResponse> allModels() {
        Optional<List<Show>> models = showService.findNDeleted();
        return ShowMapper.mapToShowResponse(models.orElse(null));
    }

    @Override
    public List<ShowResponse> allModels(String tag) {
        Optional<List<Show>> models = showService.findNDeletedByTag(tag);
        List<Show> shows = models.orElse(null);
        List<Show> showsFilter = shows.stream()
                .filter(show -> show.getTags().contains(tag))
                .collect(Collectors.toList());
        return ShowMapper.mapToShowResponse(showsFilter);
    }

    @Override
    public ShowResponse getModelById(int showId) {
        log.info("FindAllMyShowService.getModelById: {}", showId);
        return showService.findById(showId)
                .map(ShowMapper::map)
                .orElse(null);
    }

    @Override
    public boolean deleteModel(long id) {
        showService.markAsDeleted(id);
        return true;
    }

    @Override
    public ShowResponse randomModel() {
        Optional<Show> optionalShow = showService.findRandomShow();
        return ShowMapper.map(optionalShow.orElse(null));
    }

    // search show by tag and title
    @Override
    public List<ShowResponse> searchShow(String search){
        Optional<List<Show>> optionalShows = showService.findByTitleOrTagsContaining(search);
        return ShowMapper.mapToShowResponse(optionalShows.orElse(null));
    }

}
