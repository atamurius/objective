package ua.objective.api.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Link to content related data
 */
@JsonAutoDetect(fieldVisibility = ANY)
public class Link {

    final String rel;
    final String href;

    public Link(String rel, String href) {
        this.rel = rel;
        this.href = href;
    }

    public static Link link(String rel, Object methodCall) {
        return new Link(rel, MvcUriComponentsBuilder.fromMethodCall(methodCall).build().encode().toString());
    }
}
