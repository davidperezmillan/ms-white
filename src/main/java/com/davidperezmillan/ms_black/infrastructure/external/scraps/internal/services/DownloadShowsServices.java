package com.davidperezmillan.ms_black.infrastructure.external.scraps.internal.services;

import com.davidperezmillan.ms_black.infrastructure.external.scraps.models.MyClub;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Log4j2
public class DownloadShowsServices {


    @Value("${myclub.tmp.path}")
    private String pathTmp;

    public void downloadShows(List<MyClub> shows) {
        // descargar el video
        for (MyClub mpc : shows) {
            // descargar el video en memoria, con el nombre del objeto
            try (InputStream in = Jsoup.connect(mpc.getUrl_video()).ignoreContentType(true).execute().bodyStream()) {
                mpc.setVideo(new File(pathTmp + mpc.getName_video()));
                Files.copy(in, Paths.get(pathTmp + mpc.getName_video()));
            } catch (IOException e) {
                log.error("Error downloading the video: " + e.getMessage());
            }
        }
    }

    public void deleteAllFiles(){
        File file = new File(pathTmp);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}
