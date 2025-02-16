package com.davidperezmillan.ms_black.infrastructure.external.cloud.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.util.Collections;

@Log4j2
@Service
public class NextcloudFileUploadService {

    private final RestTemplate restTemplate;

    @Value("${nextcloud.url}")
    private String nextcloudUrl;

    @Value("${nextcloud.username}")
    private String username;

    @Value("${nextcloud.password}")
    private String password;

    public NextcloudFileUploadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean uploadFile(String targetPath, File file) {
        log.debug("Uploading file to Nextcloud: {}", file.getName());
        try {
            // Construye la URL de destino
            String uploadUrl = nextcloudUrl + "/remote.php/webdav/" + targetPath;

            // Crea las cabeceras de autenticación
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);

            // Crea el recurso del fichero
            FileSystemResource resource = new FileSystemResource(file);

            // Construye la petición
            RequestEntity<FileSystemResource> requestEntity = new RequestEntity<>(
                    resource, headers, HttpMethod.PUT, URI.create(uploadUrl)
            );

            // Realiza la solicitud HTTP
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            // Verifica el estado de la respuesta
            return response.getStatusCode() == HttpStatus.CREATED;

        } catch (Exception e) {
            log.error("Error uploading file to Nextcloud: {}", e.getMessage());
            throw e;
        }
    }




public boolean addCommentToFile(String targetPath, String comment) {
        log.info("Adding comment to file in Nextcloud: {}", targetPath);
        try {
            // Construye la URL de destino para agregar el comentario
            String commentUrl = nextcloudUrl + "/remote.php/webdav/comments/files/" + targetPath;

            // Crea las cabeceras de autenticación
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crea el cuerpo de la solicitud con el comentario
            String requestBody = "{\"actorType\":\"users\",\"verb\":\"comment\",\"message\":\"" + comment + "\"}";

            // Construye la petición
            RequestEntity<String> requestEntity = new RequestEntity<>(
                    requestBody, headers, HttpMethod.POST, URI.create(commentUrl)
            );

            // Realiza la solicitud HTTP
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            // Verifica el estado de la respuesta
            return response.getStatusCode() == HttpStatus.CREATED;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
