package ua.objective.api.model;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ua.objective.api.common.Link;
import ua.objective.core.model.Type;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;
import static ua.objective.api.common.Link.link;

import java.util.Collections;
import java.util.List;

/**
 * Response bean for Type
 */
@JsonAutoDetect(fieldVisibility = ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeResp {
    final String group;
    final String name;
    final String qualifiedName;
    final boolean isAbstract;

    final List<Link> links;

    @JsonProperty("extends")
    List<TypeResp> supertypes;

    List<AttributeResp> attributes;

    public TypeResp(Type type) {
        this.group = type.getGroup();
        this.name = type.getName();
        this.isAbstract = type.isAbstract();
        this.qualifiedName = type.getQualifiedName();
        this.links = asList(
                link("self", on(TypesController.class).getType(qualifiedName)),
                link("collection", on(TypesController.class).getTypes()));
    }

    public static TypeResp full(Type type) {
        TypeResp t = new TypeResp(type);
        t.supertypes = type.getSuperTypes().stream()
                .map(TypeResp::new)
                .collect(toList());
        t.attributes = type.getAttributes().values().stream()
                .map(AttributeResp::new)
                .collect(toList());
        return t;
    }
}
