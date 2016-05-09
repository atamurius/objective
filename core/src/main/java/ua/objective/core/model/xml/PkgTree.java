package ua.objective.core.model.xml;

import java.util.Map;
import java.util.TreeMap;

/**
 * Tree of packages to reduce nesting
 * collapsing packages without types
 */
public class PkgTree {

    private static class Node {
        PackageNode pkg;
        String name;
        Map<String,Node> nodes = new TreeMap<>();

        public Node(String name) {
            this.name = name;
        }
    }

    private Node root = new Node(null);

    public TypeNode add(ua.objective.core.model.Type type) {
        String[] pts = type.getGroup().split("\\.");
        Node node = root;
        for (String pt : pts) {
            node = node.nodes.computeIfAbsent(pt, Node::new);
        }
        if (node.pkg == null) {
            node.pkg = new PackageNode(node.name);
        }
        return new TypeNode(node.pkg, type.getName());
    }

    public void fillPackages(ModelNode model) {
        root.nodes.values().forEach(n ->
                model.getPackages().add(fillPackageNames(n)));
    }

    private PackageNode fillPackageNames(Node node) {
        if (node.pkg != null || node.nodes.size() > 1) {
            PackageNode pkg = node.pkg == null
                ? new PackageNode(node.name)
                : node.pkg;

            node.nodes.values().forEach(n ->
                    pkg.getPackages().add(fillPackageNames(n)));
            return pkg;
        }
        else {
            Node next = node.nodes.values().iterator().next();
            PackageNode pkg = fillPackageNames(next);
            pkg.setName(node.name + "." + pkg.getName());
            return pkg;
        }
    }
}
