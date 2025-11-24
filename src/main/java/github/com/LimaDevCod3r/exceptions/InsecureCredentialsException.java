package github.com.LimaDevCod3r.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsecureCredentialsException extends RuntimeException {
    public InsecureCredentialsException(String message) {
        super(message);
    }
}
