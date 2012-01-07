package cs265.hf.data.access.rdf;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import cs265.hf.data.HFEdge;
import cs265.hf.data.EdgeType;
import cs265.hf.data.HFNode;
import cs265.hf.data.NodeType;

/**
 *
 */
public class RdfResourceRepository {

    private Model model;

    public RdfResourceRepository(Model model) {
        this.model = model;
    }

    public Property hasName() {
        return model.createProperty("hasName");
    }

    public Property isType() {
        return model.createProperty("isType");
    }

    public Literal name(HFNode node) {
        return name(node.getName());
    }

    public Literal name(String name) {
        return model.createLiteral(name);
    }

    public Literal type(HFNode node) {
        return type(node.getType());
    }

    public Resource node(HFNode node) {
        return node(node.getId());
    }

    public Resource node(String id) {
        return model.createResource(id);
    }

    public Property edge(HFEdge edge) {
        return edge(edge.getType());
    }

    public Property edge(EdgeType edgeType) {
        return model.createProperty(edgeType.toString());

    }

    public Literal type(NodeType nodeType) {
        return model.createLiteral(nodeType.toString());
    }
}
