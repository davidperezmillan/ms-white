package com.davidperezmillan.ms_black.infrastructure.web.controllers;

import com.davidperezmillan.ms_black.applicacion.usecase.AddTorrentUseCase;
import com.davidperezmillan.ms_black.applicacion.usecase.FindAllMyShowUseCase;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ResponseGeneric;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ShowResponse;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.TorrentResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/torrent")
public class TorrentController {

    private final AddTorrentUseCase addTorrentUseCase;


    @Autowired
    public TorrentController(AddTorrentUseCase addTorrentUseCase) {
        this.addTorrentUseCase = addTorrentUseCase;

    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseGeneric<Object>> addTorrent(@PathVariable long id) {
        addTorrentUseCase.addTorrent(id);
        ResponseGeneric<Object> response = new ResponseGeneric<>(1, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseGeneric<List<TorrentResponse>>> findAllTorrent() {
        List<TorrentResponse> torrents = addTorrentUseCase.findAllTorrent();
        ResponseGeneric<List<TorrentResponse>> response = new ResponseGeneric<>(torrents.size(), torrents);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/transmission")
    public ResponseEntity<ResponseGeneric<List<TorrentResponse>>> findAllTransmission() {
        try {
            List<TorrentResponse> torrents = addTorrentUseCase.findAllTransmission();
            ResponseGeneric<List<TorrentResponse>> response = new ResponseGeneric<>(torrents.size(), torrents);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}