package ua.objective.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.objective.core.model.xml.XmlModelStorage;

import javax.xml.bind.JAXBException;
import java.io.File;

@Configuration
public class AppConfig {

    @Bean XmlModelStorage modelStorage() throws JAXBException {
        return new XmlModelStorage(new File("target/model.xml"));
    }
}
