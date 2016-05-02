package ua.objective.core.model.xml;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

public abstract class ChildOf<T> {

    @XmlTransient
    protected T parent;

    @SuppressWarnings("unchecked")
    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.parent = (T) parent;
    }
}
