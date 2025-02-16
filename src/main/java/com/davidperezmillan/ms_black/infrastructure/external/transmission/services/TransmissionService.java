package com.davidperezmillan.ms_black.infrastructure.external.transmission.services;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.StatusTorrent;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import com.davidperezmillan.ms_black.infrastructure.external.torrent.TorrentHandlerService;
import com.davidperezmillan.ms_black.infrastructure.external.transmission.models.Arguments;
import com.davidperezmillan.ms_black.infrastructure.external.transmission.models.TransmissionRequest;
import com.davidperezmillan.ms_black.infrastructure.external.transmission.models.TransmissionResponse;
import com.davidperezmillan.ms_black.infrastructure.external.transmission.models.TransmissionTorrent;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@Log4j2
public class TransmissionService {

    private final TorrentHandlerService torrentHandlerService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String transmissionUrl = "http://192.168.68.195:9069/transmission/rpc";
    private final String username = "special";
    private final String password = "clon9897";

    private String sessionId;

    public TransmissionService(TorrentHandlerService torrentHandlerService) {
        this.torrentHandlerService = torrentHandlerService;
    }


    public List<TransmissionTorrent> listTorrent() {
        try {
            if (sessionId == null) {
                HttpHeaders headers = createHeaders(username, password);
                getResponseHeaders(headers);
            }
        } catch (ResourceAccessException e) {
            log.warn("Transmission no disponible");
            return null;
        }
        try {
            log.info("Session ID: {}", sessionId);
            // Crear la solicitud para añadir el enlace magnet
            HttpHeaders headers = createHeaders(username, password);

            headers.set("X-Transmission-Session-Id", sessionId);

            headers.set("Content-Type", "application/json");

            //String jsonPayload = "{\"method\": \"torrent-get\", \"arguments\": {\"fields\": [\"id\", \"name\", \"status\", \"percentDone\"]}}";
            TransmissionRequest request = new TransmissionRequest();
            request.setMethod("torrent-get");
            Arguments arguments = new Arguments();
            arguments.setFields(new String[]{"id", "name", "status", "percentDone", "hashString"});
            request.setArguments(arguments);

            HttpEntity<TransmissionRequest> requestEntity = new HttpEntity<>(request, headers);

        /*
        ResponseEntity<String> respuesta = restTemplate.exchange(transmissionUrl, HttpMethod.POST, requestEntity, String.class);
        log.info("Respuesta: {}", respuesta.getBody());
        */

            // Enviar la solicitud POST para añadir el torrent

            ResponseEntity<TransmissionResponse> respuesta = restTemplate.exchange(transmissionUrl, HttpMethod.POST, requestEntity, TransmissionResponse.class);

            if (null != respuesta.getBody().getArguments().getTorrents()) {
                return List.of(respuesta.getBody().getArguments().getTorrents());
            }


        } catch (HttpClientErrorException.Conflict e) {
            // Capturar el error 409 y obtener el nuevo ID de sesión
            sessionId = e.getResponseHeaders().getFirst("X-Transmission-Session-Id");
            log.info("Session ID by Error: {}", sessionId);
            if (sessionId == null) {
                throw new RuntimeException("No se pudo obtener el ID de sesión de Transmission.");
            }
        } catch (ResourceAccessException e) {  // capturamos si transmission no esta disponible
            throw new RuntimeException("Transmission no está disponible.");
        }
        return List.of();
    }


    public void addTorrent(Torrent torrent) {
        try {
            if (sessionId == null) {
                HttpHeaders headers = createHeaders(username, password);
                getResponseHeaders(headers);
            }
        } catch (ResourceAccessException e) {
            log.warn("Transmission no está disponible.");
            return;
        }
        try {
            // Crear la solicitud para añadir el enlace magnet
            HttpHeaders headers = createHeaders(username, password);

            headers.set("X-Transmission-Session-Id", sessionId);

            headers.set("Content-Type", "application/json");

            TransmissionRequest request = new TransmissionRequest();
            request.setMethod("torrent-add");

            Arguments arguments = new Arguments();
            arguments.setFilename(torrent.getMagnet());
            request.setArguments(arguments);

            HttpEntity<TransmissionRequest> requestEntity = new HttpEntity<>(request, headers);

            // Enviar la solicitud POST para añadir el torrent
            ResponseEntity<TransmissionResponse> respuesta = restTemplate.exchange(transmissionUrl, HttpMethod.POST, requestEntity, TransmissionResponse.class);

            if (respuesta.getBody().getResult().equals("success")) {
                TransmissionTorrent transmissionTorrent = new TransmissionTorrent();
                if (null != respuesta.getBody().getArguments().getTorrentDuplicate()) {
                    log.warn("Torrent duplicado: {}", respuesta.getBody().getArguments().getTorrentDuplicate());
                    transmissionTorrent = respuesta.getBody().getArguments().getTorrentDuplicate();
                } else {
                    log.info("Torrent añadido: {}", respuesta.getBody());
                    transmissionTorrent = respuesta.getBody().getArguments().getTorrentAdded();
                }
                torrent.setStatus(StatusTorrent.DOWNLOADING);
                torrent.setIdTransmission(transmissionTorrent.getId());
                torrent.setHashString(transmissionTorrent.getHashString());
                torrent.setPercentDone(transmissionTorrent.getPercentDone());
                return;
            }
            log.warn("Error al añadir el torrent: {}", respuesta.getBody().getResult());
            torrent.setStatus(StatusTorrent.PENDING);

        } catch (HttpClientErrorException.Conflict e) {
            // Capturar el error 409 y obtener el nuevo ID de sesión
            sessionId = e.getResponseHeaders().getFirst("X-Transmission-Session-Id");
            log.info("Session ID by Error: {}", sessionId);
            if (sessionId == null) {
                throw new RuntimeException("No se pudo obtener el ID de sesión de Transmission.");
            }
        } catch (ResourceAccessException e) {  // capturamos si transmission no esta disponible
            throw new RuntimeException("Transmission no está disponible.");
        }
    }

    private void getResponseHeaders(HttpHeaders headers) {
        try {
            HttpEntity<TransmissionRequest> requestEntity = new HttpEntity<>(new TransmissionRequest(), headers);
            ResponseEntity<TransmissionResponse> respuesta = restTemplate.exchange(transmissionUrl, HttpMethod.POST, requestEntity, TransmissionResponse.class);
            sessionId = respuesta.getHeaders().getFirst("X-Transmission-Session-Id");
        } catch (HttpClientErrorException.Conflict e) {
            // Capturar el error 409 y obtener el nuevo ID de sesión
            sessionId = e.getResponseHeaders().getFirst("X-Transmission-Session-Id");
            log.info("Session ID by Error: {}", sessionId);
            if (sessionId == null) {
                throw new RuntimeException("No se pudo obtener el ID de sesión de Transmission.");
            }
        } catch (ResourceAccessException e) {  // capturamos si transmission no esta disponible
            throw new RuntimeException("Transmission no está disponible.");
        }


    }


    private HttpHeaders createHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }


}
