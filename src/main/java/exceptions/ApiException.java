package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for API
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ApiException extends RuntimeException {
    public ApiException(String error) {
        super(error);
    }
}
