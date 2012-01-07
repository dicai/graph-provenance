package cs265.hf.data.access.rdf;

import cs265.hf.data.ExceptionPair;
import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import cs265.hf.data.access.DataAccess;
import cs265.hf.data.access.NodeNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class RdfDataAccess implements DataAccess {

    private final SparqlQueryFactory queryFactory;

    public RdfDataAccess() {
        queryFactory = new SparqlQueryFactory();
    }

    @Override
    public void saveNode(HFNode node) throws NodeNotFoundException {
        executeQuery(queryFactory.getInsertNodeQuery(node));
    }

    @Override
    public void saveEdge(HFEdge edge) throws NodeNotFoundException {
        executeQuery(queryFactory.getInsertEdgeQuery(edge));
    }

    @Override
    public void close() throws IOException {
        queryFactory.cleanup();
    }

    @Override
    public HFNode getProducerOf(HFNode node) throws NodeNotFoundException {
        return executeQuery(queryFactory.getProducerQuery(node));
    }

    @Override
    public List<HFNode> getDependentsOf(HFNode dataNode) throws NodeNotFoundException {
        return executeQuery(queryFactory.getDependents(dataNode));
    }

    @Override
    public List<ExceptionPair> getExceptions() throws NodeNotFoundException {
        return executeQuery(queryFactory.getExceptionQuery());
    }

    @Override
    public Long getNumExecutions(String stepName) throws NodeNotFoundException {
        return executeQuery(queryFactory.getNumExecutionQuery(stepName));
    }

    private <T> T executeQuery(SparqlQuery<T> query) throws NodeNotFoundException {
        return query.execute();
    }
}
