package ua.objective.core.model.xml;

import ua.objective.core.model.AttrType;
import ua.objective.core.model.beans.AttributeBean;
import ua.objective.core.model.beans.ModelBean;
import ua.objective.core.model.beans.TypeBean;
import ua.objective.core.model.types.DateType;
import ua.objective.core.model.types.IntType;
import ua.objective.core.model.types.TextType;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import ua.objective.core.model.Type.Builder;

public class XmlModelStorage {

    private final File file;
    private final JAXBContext ctx;

    public XmlModelStorage(File file) throws JAXBException {
        this.file = file;
        ctx = JAXBContext.newInstance(Model.class);
    }

    public void store(ua.objective.core.model.Model model) throws IOException {
        try {
            PkgTree pkgs = new PkgTree();
            model.getTypes().forEach(t -> {
                Type type = pkgs.add(t);
                type.setAbstract(t.isAbstract());
                t.getSuperTypes().forEach(st ->
                        type.extendFrom(st.getQualifiedName()));
                t.getOwnAttributes().forEach(a ->
                        type.getAttributes().add(
                                new Attribute(a.getName(),
                                a.getType().getClass().getCanonicalName())));
            });
            Model root = new Model();
            pkgs.fillPackages(root);
            createMarshaller().marshal(root, file);
        }
        catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    public void load(ua.objective.core.model.Model model) throws IOException {
        try {
            Model m = (Model) createUnmarshaller().unmarshal(file);
            loadTypes(model, "", m);
            loadSuperTypes(model, "", m);
        }
        catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    private void loadSuperTypes(ua.objective.core.model.Model model, String pkg, PkgContainer c) throws IOException {
        for (Package p : c.getPackages()) {
            String group = pkg.isEmpty() ? p.getName() : pkg +"."+ p.getName();
            for (Type t : p.getTypes()) {
                ua.objective.core.model.Type type = model.getTypeByQName(group + ":" + t.getName());
                for (String st : t.getBaseTypeNames()) {
                    ua.objective.core.model.Type sType = model.getTypeByQName(st);
                    // TODO edit using model
                    type.getSuperTypes().add(sType);
                }
            }
            loadSuperTypes(model, group, p);
        }
    }

    private void loadTypes(ua.objective.core.model.Model model,
                           String pkg, PkgContainer container) throws IOException {
        for (Package p : container.getPackages()) {
            String group = pkg.isEmpty() ? p.getName() : pkg +"."+ p.getName();
            for (Type t : p.getTypes()) {
                Builder typeBuilder = model.createType(group, t.getName());
                typeBuilder.setAbstract(t.isAbstract());
                for (Attribute a : t.getAttributes()) {
                        typeBuilder.addAttribute(a.getName(),
                                buildType(a.getType()));
                }
                typeBuilder.build();
            }
            loadTypes(model, group, p);
        }
    }

    private AttrType buildType(String type) throws IOException {
        try {
            return (AttrType) Class.forName(type).newInstance();
        } catch (ClassCastException | ReflectiveOperationException e) {
            throw new IOException("Can't build attribute type "+ type, e);
        }
    }

    private Marshaller createMarshaller() throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        return marshaller;
    }

    private Unmarshaller createUnmarshaller() throws JAXBException {
        return ctx.createUnmarshaller();
    }

    @PostConstruct
    public void createTestModel() throws IOException {
        ua.objective.core.model.beans.ModelBean model = new ua.objective.core.model.beans.ModelBean();

        ua.objective.core.model.Type versioned =
                model.createType("ua.objective.core", "Versioned")
                    .addAttribute("version", new IntType())
                    .setAbstract(true)
                    .build();

        ua.objective.core.model.Type any =
                model.createType("ua.objective.core", "Any")
                    .addAttribute("created", new DateType())
                    .addAttribute("updated", new DateType())
                    .setAbstract(true)
                    .build();

        ua.objective.core.model.Type user =
                model.createType("ua.objective.core.users", "User")
                    .addAttribute("name", new TextType())
                    .extend(any)
                    .extend(versioned)
                    .build();

        store(model);
        ua.objective.core.model.Model model1 = new ua.objective.core.model.beans.ModelBean();
        load(model1);

        System.out.println(model1);
    }
}
