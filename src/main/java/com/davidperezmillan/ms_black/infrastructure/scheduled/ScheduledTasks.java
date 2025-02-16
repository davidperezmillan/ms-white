package com.davidperezmillan.ms_black.infrastructure.scheduled;

import com.davidperezmillan.ms_black.applicacion.usecase.AddTorrentUseCase;
import com.davidperezmillan.ms_black.applicacion.usecase.FindAllMyShowUseCase;
import com.davidperezmillan.ms_black.infrastructure.bbdd.ParameterConstants;
import com.davidperezmillan.ms_black.infrastructure.bbdd.mappers.TorrentMapper;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.StatusTorrent;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.ParameterService;
import com.davidperezmillan.ms_black.infrastructure.external.transmission.services.TransmissionService;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.TorrentResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class ScheduledTasks {


    private final FindAllMyShowUseCase findAllMyShowUseCase;
    private final AddTorrentUseCase addTorrentUseCase;
    private final TransmissionService transmissionService;
    private final ParameterService parameterService;

    public ScheduledTasks(FindAllMyShowUseCase findAllMyShowUseCase,
                          AddTorrentUseCase addTorrentUseCase,
                          TransmissionService transmissionService,
                          ParameterService parameterService) {
        this.findAllMyShowUseCase = findAllMyShowUseCase;
        this.addTorrentUseCase = addTorrentUseCase;
        this.transmissionService = transmissionService;
        this.parameterService = parameterService;
    }

    @Scheduled(cron = "#{@parameterService.getSheduledTime()}") // 0 0 */6 * * *
    public void performTask() {
        log.info("Scheduled task started");
        findAllMyShowUseCase.findAllMyShow(1);
        log.info("Scheduled task finished");
    }

    // tarea que se ejecuta cada minuto
    @Scheduled(cron = "0 */1 * * * *")
    public void performTask2() {

        log.info("Scheduled task 2 started");
        List<TorrentResponse> listaTorrent = addTorrentUseCase.findAllTorrent();
        for (Torrent torrent : TorrentMapper.mapToTorrent(listaTorrent)) {
            if (torrent.getStatus().equals(StatusTorrent.PENDING)) {
                transmissionService.addTorrent(torrent);
                log.info("Torrent added to transmission: " + torrent.getStatus());
                addTorrentUseCase.updateTorrent(torrent.getId(), torrent);
            }

        }
        try {
            addTorrentUseCase.findAllTransmission();
        } catch (Exception e) {
            log.error("Error getting transmission torrents", e);
        }
        log.info("Scheduled task 2 finished");

    }
}

