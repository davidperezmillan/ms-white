package com.davidperezmillan.ms_black.infrastructure.external.torrent;

import com.davidperezmillan.ms_black.infrastructure.bbdd.mappers.TorrentMapper;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.StatusTorrent;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.ShowService;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.TorrentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TorrentHandlerService {

    private final ShowService showService;
    private final TorrentService torrentService;

    public TorrentHandlerService(ShowService showService,
                                 TorrentService torrentService) {
        this.showService = showService;
        this.torrentService = torrentService;
    }

    public void createTorrentFromShow(long id) {
        Torrent torrent = cloneShowByTorrent(id);
        if (torrent != null) {
            torrent.setStatus(StatusTorrent.PENDING);
            torrentService.save(torrent);
        }
    }


    private Torrent cloneShowByTorrent(long id) {
        Optional<Show> show = showService.findById(id);
        if (show.isPresent()) {
            Torrent torrent = TorrentMapper.map(show.get());
            return torrent;
        }
        return null;
    }


    public List<Torrent> findAll() {
        return torrentService.findAll();

    }

    public void updateTorrent(long id, Torrent torrent) {
        Optional<Torrent> torrentOptional = torrentService.findById(id);
        if (torrentOptional.isPresent()) {
            torrent.setId(id);
            torrentService.save(torrent);
        }
    }

    public Torrent getTorrentByHashString(String hashString) {
        return torrentService.findByHashString(hashString);
    }
}
