package cs265.hf.data.access.sql;

import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import cs265.hf.data.access.DataLoader;
import cs265.hf.data.access.NodeNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author loren
 */
public class SqlDataLoader implements DataLoader {

    private SqlQueryFactory factory;
    private DataSource dataSource;
    private Connection conn = null;

    public SqlDataLoader() {
        this.factory = new SqlQueryFactory();
        this.dataSource = DataSourceFactory.getDataSource();
    }

    @Override
    public void begin() throws IOException {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void saveNode(HFNode node) throws IOException, NodeNotFoundException {
        runQuery(factory.getInsertNodeQuery(node));
    }

    @Override
    public void saveEdge(HFEdge edge) throws IOException, NodeNotFoundException {
        runQuery(factory.getInsertEdgeQuery(edge));
    }

    @Override
    public void end() throws IOException {
        try {
            conn.commit();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void fail() throws IOException {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void cleanup() throws IOException {
        Closer.close(conn);
    }

    @Override
    public void init() throws IOException {
        try {
            conn = dataSource.getConnection();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    private <T> T runQuery(SqlQuery<T> sqlQuery) throws NodeNotFoundException, IOException {
        try {
            return sqlQuery.execute(conn);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
}
