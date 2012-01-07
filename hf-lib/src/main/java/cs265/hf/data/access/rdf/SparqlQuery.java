package cs265.hf.data.access.rdf;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import cs265.hf.data.HFNode;
import cs265.hf.data.NodeType;
import cs265.hf.data.access.NodeNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public abstract class SparqlQuery<T> {

    private static Model model;
    private static QueryBindingFactory bindingFactory;

    public static void setModel(Model model) {
        SparqlQuery.model = model;
    }

    public static void setResourceRepository(RdfResourceRepository rRepo) {
        SparqlQuery.bindingFactory = new QueryBindingFactory(rRepo);
    }

    public T execute() throws NodeNotFoundException {
        QueryExecution qexec = createQueryExecution();
        try {
            return getResults(qexec.execSelect());
        } finally {
            qexec.close();
        }
    }

    private QueryExecution createQueryExecution() {
        Query query = QueryFactory.create(getQuery(), Syntax.syntaxARQ);
        return QueryExecutionFactory.create(query, model, getBindings());
    }

    private QuerySolutionMap getBindings() {
        QuerySolutionMap bindings = new QuerySolutionMap();
        bindings.addAll(bindingFactory.getBindings());
        getVarBindings(bindings);
        return bindings;
    }

    protected HFNode getNodeFromSolution(QuerySolution qs) {
        HFNode node = new HFNode();
        node.setId(qs.get("node_id").toString());
        node.setType(NodeType.fromString(qs.get("node_type").toString()));
        node.setName(qs.get("node_name").toString());
        return node;
    }

    protected List<HFNode> getNodesFromResultSet(ResultSet resultSet) {
        List<HFNode> nodes = new LinkedList<HFNode>();
        while (resultSet.hasNext()) {
            nodes.add(getNodeFromSolution(resultSet.next()));
        }
        return nodes;
    }

    protected abstract String getQuery();

    protected abstract void getVarBindings(QuerySolutionMap querySolutionMap);

    protected abstract T getResults(ResultSet resultSet) throws NodeNotFoundException;
}
