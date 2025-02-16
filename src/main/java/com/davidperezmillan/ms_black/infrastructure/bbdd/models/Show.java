package com.davidperezmillan.ms_black.infrastructure.bbdd.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "`show`")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String showId;

    private String title;
    private String url_page;
    private String name_video;
    private String url_video;
    private String url_image;
    private Date date;
    private String size;

    @Column(length = 1024)
    private String tags;
    @Column(length = 1024) // Increase the size limit
    private String magnet;

    private boolean deleted;

}
