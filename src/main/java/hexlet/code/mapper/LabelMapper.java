package hexlet.code.mapper;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.utils.SlugUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    LabelDto toDto(Label label);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", expression = "java(hexlet.code.utils.SlugUtils.slugify(dto.getName()))")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Label fromCreate(LabelCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(@MappingTarget Label label, LabelUpdateDto dto);

    @AfterMapping
    default void afterUpdate(@MappingTarget Label label, LabelUpdateDto dto) {
        if (dto.getName() != null) {
            label.setSlug(SlugUtils.slugify(dto.getName()));
        }
    }
}
