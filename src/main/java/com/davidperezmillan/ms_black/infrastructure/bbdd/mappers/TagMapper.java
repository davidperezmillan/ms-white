package com.davidperezmillan.ms_black.infrastructure.bbdd.mappers;


import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Tag;
import com.davidperezmillan.ms_black.infrastructure.external.scraps.models.MyClub;
import org.modelmapper.ModelMapper;

public class TagMapper {

   public static Tag map(String tag) {
       ModelMapper modelMapper = new ModelMapper();
       modelMapper.createTypeMap(String.class, Tag.class)
               .addMapping(src -> src, Tag::setName);

       return modelMapper.map(tag, Tag.class);
   }

}
