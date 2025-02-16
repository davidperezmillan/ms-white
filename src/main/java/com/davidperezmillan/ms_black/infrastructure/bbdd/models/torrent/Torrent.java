package com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "`torrent`")
public class Torrent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String showId;

    @Column(name = "id_show")
    private String idShow;

    private String title;

    @Column(length = 1024) // Increase the size limit
    private String magnet;

    private StatusTorrent status;

    private String hashString;

    private double percentDone;

    @Column(name = "id_transmission")
    private int idTransmission;

}
