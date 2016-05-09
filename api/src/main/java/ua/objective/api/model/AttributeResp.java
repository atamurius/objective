package ua.objective.api.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ua.objective.core.model.Attribute;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public class AttributeResp {

    final String owner;
    final String name;
    final String type;

    public AttributeResp(Attribute attr) {
        this.owner = attr.getOwner().getQualifiedName();
        this.name = attr.getName();
        this.type = attr.getType().getName();
    }
}
