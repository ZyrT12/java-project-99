package hexlet.code.mapper;

import hexlet.code.dto.labels.LabelDto;
import hexlet.code.model.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelDto toDto(Label label);
}
