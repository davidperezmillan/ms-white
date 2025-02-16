package com.davidperezmillan.ms_black.infrastructure.external.scraps.internal.services;

import com.davidperezmillan.ms_black.infrastructure.external.cloud.services.NextcloudFileUploadService;
import com.davidperezmillan.ms_black.infrastructure.external.scraps.models.MyClub;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class UploadShowsServices {

    private final NextcloudFileUploadService nextcloudFileUploadService;

    public UploadShowsServices(NextcloudFileUploadService nextcloudFileUploadService) {
        this.nextcloudFileUploadService = nextcloudFileUploadService;
    }

    public void uploadShows(List<MyClub> shows) {

        for (MyClub mpc : shows) {
            // subir el fichero a nextcloud
            String remotePath = upload(mpc);
            if (remotePath != null) {
                        /*
                        try{
                            boolean comment = nextcloudFileUploadService.addCommentToFile(uniquePath, mpc.getTitle());
                        }catch (Exception e){
                            log.error("Error adding comment to file: " + e.getMessage());
                        }*/
            }
        }
    }

    private String upload(MyClub mpc) {
        try {
            String uniquePath = "";
            boolean created = false;
            // join tags
            String tags = String.join("_", mpc.getTags());
            String fileName = mpc.getName_video().substring(0, mpc.getName_video().lastIndexOf('.'));
            String fileExtension = mpc.getName_video().substring(mpc.getName_video().lastIndexOf('.'));
            String nameUpload = fileName + "_" + tags + fileExtension;
            for (String tag : mpc.getTags()) {
                uniquePath = "dev/" + tag + "/" + nameUpload;
                try {
                    created = nextcloudFileUploadService.uploadFile(uniquePath, mpc.getVideo());
                    if (created) {
                        log.info("File uploaded: {}", uniquePath);
                        return uniquePath;
                    }
                } catch (Exception e) {
                    log.warn("No se a podido subir el fichero: {}", uniquePath);
                }
            }
            if (!created) {
                uniquePath = "dev/" + nameUpload;
                log.info("file upload to defect path: {}", uniquePath);
                created = nextcloudFileUploadService.uploadFile(uniquePath, mpc.getVideo());
                if (created) {
                    log.info("File uploaded: {}", uniquePath);
                    return uniquePath;
                }
                log.info("file loss: {}", mpc.getTitle());
                return null;
            }


        } catch (Exception e) {
            log.error("Error uploading the video: " + e.getMessage());
        }
        return null;
    }
}
