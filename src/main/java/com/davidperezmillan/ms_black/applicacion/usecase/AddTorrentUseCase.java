package com.davidperezmillan.ms_black.applicacion.usecase;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ShowResponse;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.TorrentResponse;

import java.util.List;

public interface AddTorrentUseCase {

    void addTorrent(long id);

    void updateTorrent(long id, Torrent torrent);

    List<TorrentResponse> findAllTorrent();

    List<TorrentResponse> findAllTransmission() throws Exception;
}
