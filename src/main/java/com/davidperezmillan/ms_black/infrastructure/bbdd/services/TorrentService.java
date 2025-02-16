package com.davidperezmillan.ms_black.infrastructure.bbdd.services;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import com.davidperezmillan.ms_black.infrastructure.bbdd.repositories.TorrentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TorrentService {

    private final TorrentRepository torrentRepository;

    public TorrentService(TorrentRepository torrentRepository) {
        this.torrentRepository = torrentRepository;
    }

    public void save(Torrent torrent) {
        try {
            torrentRepository.save(torrent);
        } catch (DataIntegrityViolationException e) {
            log.warn("Error saving duplicate torrent: {} - {}", e.getMessage(), torrent.getTitle());
            Torrent torrentOriginal = torrentRepository.findByShowId(torrent.getShowId()).get();
            torrentOriginal.setStatus(torrent.getStatus());
            torrentRepository.save(torrentOriginal);
        } catch (Exception e) {
            log.error("Error saving torrent: {}", e.getMessage());
        }

    }

    public Optional<Torrent> findById(long id) {
        return torrentRepository.findById(id);
    }

    public List<Torrent> findAll() {
        return torrentRepository.findAll();
    }

    public Torrent findByHashString(String hashString){
        return torrentRepository.findByHashString(hashString);
    }
}
