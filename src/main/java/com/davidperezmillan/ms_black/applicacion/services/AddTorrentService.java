package com.davidperezmillan.ms_black.applicacion.services;

import com.davidperezmillan.ms_black.applicacion.usecase.AddTorrentUseCase;
import com.davidperezmillan.ms_black.infrastructure.bbdd.mappers.TorrentMapper;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.StatusTorrent;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import com.davidperezmillan.ms_black.infrastructure.external.torrent.TorrentHandlerService;
import com.davidperezmillan.ms_black.infrastructure.external.transmission.models.TransmissionTorrent;
import com.davidperezmillan.ms_black.infrastructure.external.transmission.services.TransmissionService;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.TorrentResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class AddTorrentService implements AddTorrentUseCase {

    private final TorrentHandlerService torrentHandlerService;
    private final TransmissionService transmissionService;



    public AddTorrentService(TorrentHandlerService torrentHandlerService,
                             TransmissionService transmissionService) {
        this.torrentHandlerService = torrentHandlerService;
        this.transmissionService = transmissionService;
    }

    @Override
    public void addTorrent(long id) {
        log.info("Adding torrent for show: {}", id);
        torrentHandlerService.createTorrentFromShow(id);
    }

    @Override
    public void updateTorrent(long id, Torrent torrent) {
        torrentHandlerService.updateTorrent(id, torrent);
    }

    @Override
    public List<TorrentResponse> findAllTorrent() {
        return TorrentMapper.map(torrentHandlerService.findAll());
    }

    @Override
    public List<TorrentResponse> findAllTransmission() throws Exception {
        List<TransmissionTorrent> transmissionTorrents = transmissionService.listTorrent();
        for (TransmissionTorrent transmissionTorrent : transmissionTorrents) {
            log.info("Transmission torrent: {}", transmissionTorrent);
            Torrent torrent = torrentHandlerService.getTorrentByHashString(transmissionTorrent.getHashString());
            if (torrent != null) {
                log.info("Torrent found: {}", torrent);
                switch (transmissionTorrent.getStatus()){
                    case DOWNLOADING -> {
                        torrent.setStatus(StatusTorrent.DOWNLOADING);
                        torrent.setPercentDone(transmissionTorrent.getPercentDone());
                    }
                    case SEENDING -> {
                        torrent.setStatus(StatusTorrent.COMPLETE);
                        torrent.setPercentDone(transmissionTorrent.getPercentDone());
                    }
                }
                torrentHandlerService.updateTorrent(torrent.getId(), torrent);
            }
        }
        return List.of();
    }

}
