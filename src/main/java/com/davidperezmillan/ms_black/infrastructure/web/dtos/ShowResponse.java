package com.davidperezmillan.ms_black.infrastructure.web.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class ShowResponse {

    private long id;
    private String showId;
    private String title;
    private String url_page;
    private String name_video;
    private String url_video;
    private String url_image;
    private String magnet;
    private Date date;
    private String size;
    private ArrayList<String> tags;
}
