package com.davidperezmillan.ms_black.infrastructure.bbdd.mappers;


import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.torrent.Torrent;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.TorrentResponse;
import org.modelmapper.ModelMapper;

import java.util.List;

public class TorrentMapper {

    public static Torrent map(Show source) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Show.class, Torrent.class)
                .addMappings(mapper -> mapper.skip(Torrent::setId)) // skip id
                .addMapping(Show::getId, Torrent::setIdShow);
        return modelMapper.map(source, Torrent.class);
    }

    public static TorrentResponse map(Torrent source) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, TorrentResponse.class);
    }

    public static List<TorrentResponse> map(List<Torrent> source) {
        return source.stream()
                .map(TorrentMapper::map)
                .toList();
    }

    public static Torrent map(TorrentResponse source) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, Torrent.class);
    }

    public static List<Torrent> mapToTorrent(List<TorrentResponse> source) {
        return source.stream()
                .map(TorrentMapper::map)
                .toList();
    }

}
