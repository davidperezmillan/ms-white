package com.davidperezmillan.ms_black.infrastructure.external.transmission.models;

import lombok.Data;

@Data
public class TransmissionResponse {


    // {"arguments":{"torrent-added":{"hashString":"549219ff8e36a84966f80aa3209c78856d8ad5df","id":9,"name":"New.Sakura.Hell.Loantown.25.02.06.BigTits.Brunette.Hardcore.DemonINC.https.bigwarp.io.embed.vlbb613atjlu.html.mp4"}},"result":"success"}
    private Arguments arguments;

    private String result;
}
