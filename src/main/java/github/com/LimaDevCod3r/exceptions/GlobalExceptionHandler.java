package github.com.LimaDevCod3r.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    // Tratamento específico para ResourcesAlreadyExistsException
    @ExceptionHandler(ResourcesAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleResourcesAlreadyExistsException(ResourcesAlreadyExistsException ex, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    // Tratamento específico para InsecureCredentialsException
    @ExceptionHandler(InsecureCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInsecureCredentialsException(InsecureCredentialsException ex, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
