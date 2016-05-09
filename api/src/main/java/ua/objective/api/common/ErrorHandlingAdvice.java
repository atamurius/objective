package ua.objective.api.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorHandlingAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result<Void> dataNotFound(NoSuchElementException e) {
        return Result.failed("Requested data not found: "+ e.getMessage(), e);
    }

    @ExceptionHandler
    @ResponseBody
    public Result<Void> formatException(Exception e) {
        log.debug("Request failed", e);
        return Result.failed(e);
    }
}
