package github.com.LimaDevCod3r.exceptions;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}
