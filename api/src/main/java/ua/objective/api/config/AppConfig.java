package ua.objective.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.objective.core.model.Model;
import ua.objective.core.model.ModelStorage;
import ua.objective.core.model.beans.ModelBean;
import ua.objective.core.model.xml.XmlModelStorage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean ModelStorage modelStorage() throws JAXBException {
        return new XmlModelStorage(new File("target/model.xml"));
    }

    @Bean Model model(ModelStorage storage) throws IOException {
        ModelBean model = new ModelBean();
        storage.load(model);
        return model;
    }
}
