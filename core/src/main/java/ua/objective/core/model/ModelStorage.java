package ua.objective.core.model;

import ua.objective.core.model.beans.ModelBean;

import java.io.IOException;

/**
 * Object that loads and stores model
 */
public interface ModelStorage {

    void store(ModelBean model) throws IOException;

    void load(ModelBean model) throws IOException;
}
