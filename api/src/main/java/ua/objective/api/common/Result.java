package ua.objective.api.common;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Common result structure
 */
@JsonAutoDetect(fieldVisibility = ANY)
@JsonInclude(NON_NULL)
public class Result<T> {

    final boolean success;
    final String message;
    final List<String> causes;
    final T content;

    public Result(T content) {
        this.content = content;
        success = true;
        message = null;
        causes = null;
    }

    public static <T> Result<T> just(T content) {
        return new Result<>(content);
    }

    public Result(String msg, Throwable t) {
        success = false;
        message = msg;
        content = null;
        causes = new ArrayList<>();
        while (t != null) {
            causes.add(t.getClass().getCanonicalName() +": "+
                t.getMessage());
            t = t.getCause();
        }
    }

    public static <T> Result<T> failed(Throwable t) {
        return new Result<>(t.getMessage(), t);
    }

    public static <T> Result<T> failed(String msg, Throwable t) {
        return new Result<>(msg, t);
    }
}
