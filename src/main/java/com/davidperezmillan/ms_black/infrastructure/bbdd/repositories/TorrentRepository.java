package com.davidperezmillan.ms_black.infrastructure.bbdd.repositories;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TorrentRepository extends JpaRepository<Torrent, Long> {

    Optional<Torrent> findByShowId(String showId);


    Torrent findByHashString(String hashString);

    Torrent findByIdTransmission(int id);
}
