package in.balu.exceptions;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorPageRegistrar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorPageRegistrar errorPageRegistrar;
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorAPI> handleException(ResourceNotFoundException ex) {

        log.error("Resource Found Exception {}", ex.getMessage());

        ErrorAPI errorAPI = new ErrorAPI();
        errorAPI.setLocalDateTime(LocalDateTime.now());
        errorAPI.setMessage(ex.getMessage());
        errorAPI.setError("Validation Error");
        errorAPI.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());

        return new ResponseEntity<>(errorAPI, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorAPI> handleException(Exception ex) {

        log.error("Internal Server Error {}", ex.getMessage());

        ErrorAPI errorAPI = new ErrorAPI();
        errorAPI.setLocalDateTime(LocalDateTime.now());
        errorAPI.setMessage(ex.getMessage());
        errorAPI.setError("Something went wrong");
        errorAPI.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return new ResponseEntity<>(errorAPI, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorAPI> handleException(MethodArgumentNotValidException ex) {

        log.error("MethodArgumentNotValidException {}", ex.getMessage());

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(obj -> obj.getField()+" "+obj.getDefaultMessage())
                .collect(Collectors.joining(","));

        ErrorAPI errorAPI = new ErrorAPI();
        errorAPI.setLocalDateTime(LocalDateTime.now());
        errorAPI.setMessage(errors);
        errorAPI.setError("Client Side Validation Error");
        errorAPI.setStatus(HttpStatus.BAD_GATEWAY.getReasonPhrase());

        return new ResponseEntity<>(errorAPI, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorAPI> handleException(HttpMessageNotReadableException ex) {

        log.error("HttpMessageNotReadableException Exception {}", ex.getMessage());

        ErrorAPI errorAPI = new ErrorAPI();
        errorAPI.setLocalDateTime(LocalDateTime.now());
        errorAPI.setMessage(ex.getMessage());
        errorAPI.setError("Malformed JSON data");
        errorAPI.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());

        return new ResponseEntity<>(errorAPI, HttpStatus.BAD_REQUEST);
    }
}
