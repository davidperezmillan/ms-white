package com.davidperezmillan.ms_black.infrastructure.web.dtos;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.StatusTorrent;
import jakarta.persistence.*;
import lombok.Data;

@Data

public class TorrentResponse {

    private long id;

    private String showId;
    private String idShow;
    private String title;
    private String magnet;
    private StatusTorrent status;
    private double percentDone;

}
