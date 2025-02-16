package com.davidperezmillan.ms_black.infrastructure.external.scraps.services;

import com.davidperezmillan.ms_black.applicacion.ports.ScrapShowPort;
import com.davidperezmillan.ms_black.infrastructure.bbdd.mappers.ShowMapper;
import com.davidperezmillan.ms_black.infrastructure.bbdd.mappers.TagMapper;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Tag;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.ShowService;
import com.davidperezmillan.ms_black.infrastructure.bbdd.services.TagService;
import com.davidperezmillan.ms_black.infrastructure.external.scraps.internal.services.DownloadShowsServices;
import com.davidperezmillan.ms_black.infrastructure.external.scraps.internal.services.UploadShowsServices;
import com.davidperezmillan.ms_black.infrastructure.external.scraps.models.MyClub;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class ScrapMyShowClubService implements ScrapShowPort {

    @Value("${myclub.url}")
    private String domainUrl;


    @Value("${myclub.tmp.path}")
    private String pathTmp;


    private final DownloadShowsServices downloadShowsServices;
    private final UploadShowsServices uploadShowsServices;
    private final ShowService showService;
    private final TagService tagService;

    public ScrapMyShowClubService(DownloadShowsServices downloadShowsServices,
                                  UploadShowsServices uploadShowsServices,
                                  ShowService showService,
                                  TagService tagService) {
        this.downloadShowsServices = downloadShowsServices;
        this.uploadShowsServices = uploadShowsServices;
        this.showService = showService;
        this.tagService = tagService;
    }

    @Override
    public int findAll(int contador) {
        List<MyClub> shows = new ArrayList<MyClub>();
        int response = 0;
        for (int i = 0; i < contador; i++) {
            shows.addAll(execute(domainUrl + "/ts" + (i == 0 ? "" : "/" + i)));
        }

        // filtrar los shows que no tienen video
        int elementosOriginales = shows.size();
        filtrarShows(shows);
        log.info("Shows with video: {} / {}", shows.size(), elementosOriginales);
        response = shows.size();

        // registrar tags
        //registrarTags(shows);
        //topTags(5);

        // guardar tags
        CompletableFuture.runAsync(() -> saveTagsAsync(shows));

        // guardar en BBDD
        showService.saveShows(ShowMapper.map(shows));




            /*
            // descargar el video
            downloadShowsServices.downloadShows(shows);

            // subir el video a nextcloud
            uploadShowsServices.uploadShows(shows);

            // borrar los ficheros
            downloadShowsServices.deleteAllFiles();
            */

        // logger list
        //shows.forEach(show -> log.info(show));
        return response;
    }

    public List<MyClub> execute(String url) {
        List<MyClub> shows = new ArrayList<MyClub>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("torrent_element");
            for (Element element : elements) {
                MyClub myClub = MyClub.builder().build();
                String id = element.select("a[class=tdn]").attr("href").split("/")[2];
                myClub.setShowId(id);
                myClub.setDate(new Date());
                myClub.setUrl_page(domainUrl + element.select("a[class=tdn]").attr("href"));
                myClub.setTitle(sanitizeTitle(element.select("span[class=torrent_element_text_span]").first().ownText()));

                shows.add(myClub);
            }
            // recuperar video de cada show
            for (MyClub mpc : shows) {
                doc = Jsoup.connect(mpc.getUrl_page()).get();

                String size = doc.select("span[class=tsize_span]").text();
                mpc.setSize(size);

                // recuperar el data content de torrent_links
                String torrentLinks = doc.select("div[class=torrent_links]").attr("data-content");
                String urlVideo = sanitizeUrlVideo(torrentLinks);
                mpc.setUrl_video(urlVideo);
                mpc.setName_video(mpc.getShowId() + ".mp4");

                String urlImage = sanitizeUrlImage(torrentLinks);
                mpc.setUrl_image(urlImage);


                // recuparer todos tags
                Elements tdn = doc.select("div[class=htag_rel]");
                ArrayList<String> tags = new ArrayList<>();
                for (Element tag : tdn) {
                    tags.add(tag.text().replace("#", "").toLowerCase());
                }
                mpc.setTags(tags);

                // recuperar el magnet
                String magnet = doc.select("a[class=tdn d_btn md_btn]").attr("href");
                mpc.setMagnet(magnet);

            }

        } catch (IOException e) {
            log.error("Error scraping the website: " + e.getMessage());

        }

        return shows;
    }


    @Async
    private void saveTagsAsync(List<MyClub> shows) {
        // guardar tags
        for (MyClub show : shows) {
            for (String tag : show.getTags()) {
                tagService.save(TagMapper.map(tag));
            }
        }
        //Optional<List<Tag>> listTags = tagService.findAllTags();
        //if (listTags.isPresent()) {
        //    listTags.get().forEach(tag -> log.info(tag));
        //}
    }

    private void filtrarShows(List<MyClub> shows) {
        shows.removeIf(show -> show.getUrl_video() == null);
        // comprobar que el fichero no existe
        shows.removeIf(show -> new File(pathTmp + show.getName_video()).exists());
        shows.removeIf(show -> showService.findByShowId(show.getShowId()).isPresent());
    }

    private String sanitizeUrlVideo(String torrentLinks) {
        // convertir a un string [\"//sxyprn.com/post/67975e433f2f1.html\",\"//s6.trafficdeposit.com/blog/vid/618afb5ec39a8/67975e433f2f1/vidthumb.mp4\",\"//s6.trafficdeposit.com/blog/vid/618afb5ec39a8/67975e433f2f1/full.jpg\"] a array
        torrentLinks = torrentLinks.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .replace("\\", "")
                .replace("//", "");
        String[] links = torrentLinks.split(",");
        // recuperar del string el enlace del video
        for (String link : links) {
            if (link.contains(".mp4")) {
                return "https://" + link;
            }
        }
        return null;
    }

    private String sanitizeUrlImage(String torrentLinks) {
        // convertir a un string [\"//sxyprn.com/post/67975e433f2f1.html\",\"//s6.trafficdeposit.com/blog/vid/618afb5ec39a8/67975e433f2f1/vidthumb.mp4\",\"//s6.trafficdeposit.com/blog/vid/618afb5ec39a8/67975e433f2f1/full.jpg\"] a array
        torrentLinks = torrentLinks.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .replace("\\", "")
                .replace("//", "");
        String[] links = torrentLinks.split(",");
        // recuperar del string el enlace del video
        for (String link : links) {
            if (link.contains(".jpg")) {
                return "https://" + link;
            }
        }
        return null;
    }

    private String sanitizeTitle(String title){
        // si encuentra una url, borrar hasta el final de la url
        title = title.replaceAll("http\\S+", "");
        // borrar "->"
        title = title.replaceAll("->", "");
        // borrar texto raro
        title = title.replaceAll("Watch/Download:", "");

        // borrar espacios al principio y al final
        title = title.trim();


        return title;


    }

}

