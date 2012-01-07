package cs265.hf.data.access.sql;

import cs265.hf.data.ExceptionPair;
import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import cs265.hf.data.access.DataAccess;
import cs265.hf.data.access.NodeNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author loren
 */
public class SqlDataAccess implements DataAccess {

    private SqlQueryFactory factory;
    private DataSource dataSource;

    public SqlDataAccess() {
        this.factory = new SqlQueryFactory();
        this.dataSource = DataSourceFactory.getDataSource();
    }

    @Override
    public List<HFNode> getDependentsOf(HFNode dataNode) throws IOException, NodeNotFoundException {
        return runQuery(factory.getDependentsQuery(dataNode));
    }

    @Override
    public List<ExceptionPair> getExceptions() throws IOException, NodeNotFoundException {
        return runQuery(factory.getExceptionQuery());
    }

    @Override
    public Long getNumExecutions(String stepName) throws IOException, NodeNotFoundException {
        return runQuery(factory.getNumExecutionQuery(stepName));
    }

    @Override
    public HFNode getProducerOf(HFNode node) throws IOException, NodeNotFoundException {
        return runQuery(factory.getProducerQuery(node));
    }

    @Override
    public void saveEdge(HFEdge edge) throws IOException, NodeNotFoundException {
        runQuery(factory.getInsertEdgeQuery(edge));
    }

    @Override
    public void saveNode(HFNode node) throws IOException, NodeNotFoundException {
        runQuery(factory.getInsertNodeQuery(node));
    }

    private <T> T runQuery(SqlQuery<T> sqlQuery) throws NodeNotFoundException, IOException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return sqlQuery.execute(conn);
        } catch (SQLException ex) {
            throw new IOException(ex);
        } finally {
            Closer.close(conn);
        }
    }

    @Override
    public void close() throws IOException {
        //does nothing
    }
}
