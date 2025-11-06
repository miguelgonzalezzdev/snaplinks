package com.github.miguelgonzalezzdev.snaplinks.mappers;

import com.github.miguelgonzalezzdev.snaplinks.dtos.ShortUrlResponse;
import com.github.miguelgonzalezzdev.snaplinks.models.ShortUrl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShortUrlMapper {

    ShortUrlMapper INSTANCE = Mappers.getMapper(ShortUrlMapper.class);

    // De entidad a DTO
    ShortUrlResponse toDto(ShortUrl entity);
    List<ShortUrlResponse> toDtoList(List<ShortUrl> entities);

    // De DTO a entidad
    ShortUrl toEntity(ShortUrlResponse dto);
}
