package com.davidperezmillan.ms_black.infrastructure.bbdd.mappers;


import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.external.scraps.models.MyClub;
import com.davidperezmillan.ms_black.infrastructure.web.dtos.ShowResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShowMapper {

   public static Show map(MyClub source) {
       ModelMapper modelMapper = new ModelMapper();
       modelMapper.createTypeMap(MyClub.class, Show.class)
               .addMappings(mapper -> mapper.skip(Show::setId)) // skip id
               .addMapping(MyClub::getShowId, Show::setShowId);



       return modelMapper.map(source, Show.class);
   }

    public static List<Show> map(List<MyClub> source) {
        return source.stream()
                .map(ShowMapper::map)
                .toList();
    }



    public static ShowResponse map(Show source) {
        ModelMapper modelMapper = new ModelMapper();
        // Define the converter
        Converter<String, List<String>> tagsConverter = new Converter<String, List<String>>() {
            @Override
            public List<String> convert(MappingContext<String, List<String>> context) {
                String source = context.getSource();
                if (source == null || source.isEmpty()) {
                    return List.of();
                }
                return Arrays.stream(source.replace("[", "").replace("]", "").split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
            }
        };
        modelMapper.createTypeMap(Show.class, ShowResponse.class)
                .addMappings(mapper -> mapper.using(tagsConverter).map(Show::getTags, ShowResponse::setTags));

        return modelMapper.map(source, ShowResponse.class);
    }

    public static List<ShowResponse> mapToShowResponse(List<Show> source) {
        return source.stream()
                .map(ShowMapper::map)
                .toList();
    }
}
