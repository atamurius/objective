package ua.objective.core.model.xml;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class XmlModelStorage {

    private final File file;
    private final JAXBContext ctx;

    public XmlModelStorage(File file) throws JAXBException {
        this.file = file;
        ctx = JAXBContext.newInstance(Model.class);
    }

    @PostConstruct
    public void createTestModel() throws JAXBException {
        Model model = new Model();

        Package a = new Package(model, "ua");
        Package b = new Package(a, "objective");
        Package c = new Package(b, "core");

        Type any = new Type(c, "Any");
        any.setIsAbstract(true);

        Type versioned = new Type(c, "Versioned");
        versioned.setIsAbstract(true);

        Type user = new Type(c, "User");
        user.extendFrom(versioned);
        user.extendFrom(any);

        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(model, file);
    }
}
