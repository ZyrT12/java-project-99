package hexlet.code.mapper;

import hexlet.code.dto.labels.LabelResponseDto;
import hexlet.code.model.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelResponseDto toDto(Label label);
}
