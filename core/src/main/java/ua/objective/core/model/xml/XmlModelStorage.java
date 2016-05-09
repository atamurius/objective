package ua.objective.core.model.xml;

import ua.objective.core.model.AttrType;
import ua.objective.core.model.ModelStorage;
import ua.objective.core.model.Type;
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

public class XmlModelStorage implements ModelStorage {

    private final File file;
    private final JAXBContext ctx;

    public XmlModelStorage(File file) throws JAXBException {
        this.file = file;
        ctx = JAXBContext.newInstance(ModelNode.class);
    }

    @Override
    public void store(ModelBean model) throws IOException {
        try {
            PkgTree pkgs = new PkgTree();
            model.getTypes().forEach(t -> {
                TypeNode typeNode = pkgs.add(t);
                typeNode.setAbstract(t.isAbstract());
                t.getSuperTypes().forEach(st ->
                        typeNode.extendFrom(st.getQualifiedName()));
                t.getOwnAttributes().forEach(a ->
                        typeNode.getAttributes().add(
                                new AttributeNode(a.getName(),
                                a.getType().getClass().getCanonicalName())));
            });
            ModelNode root = new ModelNode();
            pkgs.fillPackages(root);
            createMarshaller().marshal(root, file);
        }
        catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void load(ModelBean model) throws IOException {
        try {
            ModelNode m = (ModelNode) createUnmarshaller().unmarshal(file);
            loadTypes(model, "", m);
            loadSuperTypes(model, "", m);
        }
        catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    private void loadSuperTypes(ModelBean model, String pkg, PkgContainer c) throws IOException {
        for (PackageNode p : c.getPackages()) {
            String group = pkg.isEmpty() ? p.getName() : pkg + Type.GROUP_SEPARATOR + p.getName();
            for (TypeNode t : p.getTypes()) {
                TypeBean type = model.getTypeByQName(group + Type.TYPE_SEPARATOR + t.getName());
                for (String st : t.getBaseTypeNames()) {
                    TypeBean sType = model.getTypeByQName(st);
                    model.extend(type, sType);
                }
            }
            loadSuperTypes(model, group, p);
        }
    }

    private void loadTypes(ModelBean model, String pkg, PkgContainer container) throws IOException {
        for (PackageNode p : container.getPackages()) {
            String group = pkg.isEmpty() ? p.getName() : pkg +"."+ p.getName();
            for (TypeNode t : p.getTypes()) {
                TypeBean type = model.createType(group, t.getName());
                model.changeAbstract(type, t.isAbstract());
                for (AttributeNode a : t.getAttributes()) {
                    model.addAttribute(type, a.getName(), buildType(a.getType()));
                }
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

//    @PostConstruct
//    public void createTestModel() throws IOException {
//        ModelBean model = new ModelBean();
//
//        TypeBean versioned = model.createType("ua.objective.core", "Versioned");
//        model.addAttribute(versioned, "version", new IntType());
//
//        TypeBean any = model.createType("ua.objective.core", "Any");
//        model.addAttribute(any, "created", new DateType());
//        model.addAttribute(any, "updated", new DateType());
//
//        TypeBean user = model.createType("ua.objective.core.users", "User");
//        model.addAttribute(user, "name", new TextType());
//        model.extend(user, any);
//        model.extend(user, versioned);
//        model.changeAbstract(user, false);
//
//        store(model);
//        ModelBean model1 = new ModelBean();
//        load(model1);
//
//        System.out.println(model1);
//    }
}
