package cs265.hf.data.access.rdf;

import com.hp.hpl.jena.query.QuerySolutionMap;
import cs265.hf.data.EdgeType;
import cs265.hf.data.NodeType;

/**
 *
 */
public class QueryBindingFactory {

    private final QuerySolutionMap bindings;
    private final RdfResourceRepository rRepo;

    public QueryBindingFactory(RdfResourceRepository rRepo) {
        this.rRepo = rRepo;
        bindings = new QuerySolutionMap();
        for (EdgeType edgeType : EdgeType.values()) {
            bindings.add(edgeType.toString(), rRepo.edge(edgeType));
        }
        for (NodeType nodeType : NodeType.values()) {
            bindings.add(nodeType.toString(), rRepo.type(nodeType));
        }
        bindings.add("isType", rRepo.isType());
        bindings.add("hasName", rRepo.hasName());
    }

    public QuerySolutionMap getBindings() {
        return bindings;
    }
}
