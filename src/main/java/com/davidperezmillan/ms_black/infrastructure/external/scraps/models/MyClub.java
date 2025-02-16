package com.davidperezmillan.ms_black.infrastructure.external.scraps.models;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class MyClub {

    private String showId;
    private String title;
    private String url_page;
    private String name_video;
    private String url_video;
    private String url_image;
    private String magnet;
    private File video;
    private Date date;
    private String size;
    private List<String> tags;
}
