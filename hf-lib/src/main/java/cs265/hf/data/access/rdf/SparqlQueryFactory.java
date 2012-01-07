package cs265.hf.data.access.rdf;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import cs265.hf.data.HFEdge;
import cs265.hf.data.ExceptionPair;
import cs265.hf.data.HFNode;
import cs265.hf.data.NodeType;
import cs265.hf.data.access.NodeNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class SparqlQueryFactory {

    private final Model model;
    private final RdfResourceRepository rRepo;

    public SparqlQueryFactory() {
        this.model = RdfModelFactory.getModel();
        this.rRepo = new RdfResourceRepository(model);
        SparqlQuery.setModel(model);
        SparqlQuery.setResourceRepository(rRepo);
    }

    public SparqlQuery<Integer> getInsertNodeQuery(final HFNode node) {
        return new SparqlQuery<Integer>() {

            @Override
            public Integer execute() {
                model.add(model.createLiteralStatement(
                        rRepo.node(node), rRepo.isType(), rRepo.type(node)));
                model.add(model.createStatement(
                        rRepo.node(node), rRepo.hasName(), rRepo.name(node)));
                return 1;
            }

            @Override
            protected String getQuery() {
                //does nothing
                return null;
            }

            @Override
            protected void getVarBindings(QuerySolutionMap querySolutionMap) {
                //does nothing
            }

            @Override
            protected Integer getResults(ResultSet resultSet) throws NodeNotFoundException {
                //does nothing
                return null;
            }
        };
    }

    public SparqlQuery<Integer> getInsertEdgeQuery(final HFEdge edge) {
        return new SparqlQuery<Integer>() {

            @Override
            public Integer execute() {
                model.add(model.createStatement(
                        rRepo.node(edge.getFromId()), rRepo.edge(edge),
                        rRepo.node(edge.getToId())));
                return 1;
            }

            @Override
            protected String getQuery() {
                //does nothing
                return null;
            }

            @Override
            protected void getVarBindings(QuerySolutionMap querySolutionMap) {
                //does nothing
            }

            @Override
            protected Integer getResults(ResultSet resultSet) throws NodeNotFoundException {
                //does nothing
                return null;
            }
        };
    }

    public SparqlQuery<HFNode> getProducerQuery(final HFNode node) {
        return new SparqlQuery<HFNode>() {

            @Override
            protected String getQuery() {
                return "SELECT ?node_id ?node_name ?node_type"
                        + " WHERE { ?prod_id ?DF ?node_id"
                        + " . ?node_id ?hasName ?node_name"
                        + " . ?node_id ?isType ?node_type .}";
            }

            @Override
            protected HFNode getResults(ResultSet resultSet) throws NodeNotFoundException {
                if (resultSet.hasNext()) {
                    return getNodeFromSolution(resultSet.next());
                } else {
                    throw new NodeNotFoundException(node);
                }
            }

            @Override
            protected void getVarBindings(QuerySolutionMap querySolutionMap) {
                querySolutionMap.add("prod_id", rRepo.node(node));
            }
        };
    }

    public SparqlQuery<List<HFNode>> getDependents(final HFNode dataNode) {
        return new SparqlQuery<List<HFNode>>() {

            @Override
            protected String getQuery() {
                return "SELECT ?node_id ?node_name ?node_type"
                        + " WHERE { ?node_id ?DF ?data_id"
                        + " . ?node_id ?hasName ?node_name"
                        + " . ?node_id ?isType ?node_type .}";
            }

            @Override
            protected List<HFNode> getResults(ResultSet resultSet) {
                return getNodesFromResultSet(resultSet);
            }

            @Override
            protected void getVarBindings(QuerySolutionMap querySolutionMap) {
                querySolutionMap.add("data_id", rRepo.node(dataNode));
            }
        };
    }

    public SparqlQuery<List<ExceptionPair>> getExceptionQuery() {
        return new SparqlQuery<List<ExceptionPair>>() {

            @Override
            protected String getQuery() {
                return "SELECT ?ex_id ?ex_name ?node_id ?node_type ?node_name"
                        + " WHERE {?ex_id ?isType ?" + NodeType.EXCEPTION.toString()
                        + " . ?ex_id ?DF ?node_id"
                        + " . ?ex_id ?hasName ?ex_name"
                        + " . ?node_id ?isType ?node_type"
                        + " . ?node_id ?hasName ?node_name .}";
            }

            @Override
            protected List<ExceptionPair> getResults(ResultSet resultSet) {
                List<ExceptionPair> exceptions = new LinkedList<ExceptionPair>();
                while (resultSet.hasNext()) {
                    QuerySolution qs = resultSet.next();
                    exceptions.add(getExceptionPairFromSolution(qs));
                }
                return exceptions;
            }

            private ExceptionPair getExceptionPairFromSolution(QuerySolution qs) {
                ExceptionPair pair = new ExceptionPair();
                pair.setException(getException(qs));
                pair.setThrower(getNodeFromSolution(qs));
                return pair;
            }

            private HFNode getException(QuerySolution qs) {
                HFNode node = new HFNode();
                node.setId(qs.get("ex_id").toString());
                node.setType(NodeType.EXCEPTION);
                node.setName(qs.get("ex_name").toString());
                return node;
            }

            @Override
            protected void getVarBindings(QuerySolutionMap querySolutionMap) {
                return;
            }
        };
    }

    public SparqlQuery<Long> getNumExecutionQuery(final String stepName) {
        return new SparqlQuery<Long>() {

            @Override
            protected String getQuery() {
                return "SELECT (count(?x) AS ?count) WHERE {?x ?hasName ?step_name .}";
            }

            @Override
            protected Long getResults(ResultSet resultSet) throws NodeNotFoundException {
                RDFNode count = resultSet.next().get("count");
                if (count.isLiteral()) {
                    return ((Literal) count).getLong();
                } else {
                    throw new AssertionError("RDFNode is not a literal: " + count);
                }
            }

            @Override
            protected void getVarBindings(QuerySolutionMap querySolutionMap) {
                querySolutionMap.add("step_name", rRepo.name(stepName));
            }
        };
    }

    public void cleanup() throws IOException {
        model.close();
    }
}
