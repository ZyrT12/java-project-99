package hexlet.code.dto.labels;

import java.time.Instant;

public record LabelResponseDto(
        Long id,
        String name,
        String slug,
        Instant createdAt
) {

}
