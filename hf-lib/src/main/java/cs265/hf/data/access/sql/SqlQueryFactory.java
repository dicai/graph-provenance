package cs265.hf.data.access.sql;

import cs265.hf.data.HFEdge;
import cs265.hf.data.ExceptionPair;
import cs265.hf.data.HFNode;
import cs265.hf.data.NodeType;
import cs265.hf.data.access.NodeNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author loren
 */
public class SqlQueryFactory {


    public SqlQuery<Integer> getInsertNodeQuery(final HFNode node) {
        return new SqlQuery<Integer>() {

            @Override
            public String getQuery() {
                return "INSERT INTO nodes(node_id,node_name,node_type) VALUES(?,?,?::ntype)";
            }

            @Override
            public void setParameters(PreparedStatement ps) throws SQLException {
                ps.setString(1, node.getId());
                ps.setString(2, node.getName());
                ps.setString(3, node.getType().name());
            }

            @Override
            protected Integer getResults(PreparedStatement ps) throws SQLException {
                return ps.getUpdateCount();
            }
        };
    }

    public SqlQuery<Integer> getInsertEdgeQuery(final HFEdge edge) {
        return new SqlQuery<Integer>() {

            @Override
            public String getQuery() {
                return "INSERT INTO edges(to_id,from_id,edge_type) VALUES(?,?,?::etype)";
            }

            @Override
            public void setParameters(PreparedStatement ps) throws SQLException {
                ps.setString(1, edge.getToId());
                ps.setString(2, edge.getFromId());
                ps.setString(3, edge.getType().name());
            }

            @Override
            protected Integer getResults(PreparedStatement ps) throws SQLException {
                return ps.getUpdateCount();
            }
        };
    }

    public SqlQuery<HFNode> getProducerQuery(final HFNode node) {
        return new SqlQuery<HFNode>() {

            @Override
            protected String getQuery() {
                return "SELECT node_name, node_id, node_type FROM nodes"
                        + " JOIN edges ON edges.to_id = nodes.node_id"
                        + " WHERE edges.from_id = ?";
            }

            @Override
            protected void setParameters(PreparedStatement ps) throws SQLException {
                ps.setString(1, node.getId());
            }

            @Override
            protected HFNode getResults(PreparedStatement ps) throws SQLException, NodeNotFoundException {
                ResultSet rs = ps.getResultSet();
                if (rs.next()) {
                    return getNode(rs);
                } else {
                    throw new NodeNotFoundException(node);
                }
            }
        };
    }

    public SqlQuery<List<HFNode>> getDependentsQuery(final HFNode dataNode) {
        return new SqlQuery<List<HFNode>>() {

            @Override
            protected String getQuery() {
                return "SELECT node_name, node_id, node_type FROM nodes"
                        + " JOIN edges ON edges.from_id = nodes.node_id"
                        + " WHERE edges.to_id = ?";
            }

            @Override
            protected void setParameters(PreparedStatement ps) throws SQLException {
                ps.setString(1, dataNode.getId());
            }

            @Override
            protected List<HFNode> getResults(PreparedStatement ps) throws SQLException {
                return getNodes(ps.getResultSet());
            }
        };
    }

    public SqlQuery<List<ExceptionPair>> getExceptionQuery() {
        return new SqlQuery<List<ExceptionPair>>() {

            @Override
            protected String getQuery() {
                return "SELECT ex.node_name AS ex_name, ex.node_id AS ex_id,"
                        + " ex.node_type AS ex_type, th.node_name AS node_name,"
                        + " th.node_type AS node_type, th.node_id AS node_id"
                        + " FROM nodes AS ex"
                        + " JOIN edges AS e1 ON ex.node_id = e1.from_id"
                        + " JOIN nodes AS th ON th.node_id = e1.to_id"
                        + " WHERE ex.node_type = 'EXCEPTION'";
            }

            @Override
            protected void setParameters(PreparedStatement ps) throws SQLException {
                return;
            }

            @Override
            protected List<ExceptionPair> getResults(PreparedStatement ps) throws SQLException, NodeNotFoundException {
                ResultSet rs = ps.getResultSet();
                List<ExceptionPair> pairs = new LinkedList<ExceptionPair>();
                while (rs.next()) {
                    HFNode thrower = getNode(rs);
                    HFNode ex = getExeptionNode(rs);
                    ExceptionPair pair = new ExceptionPair();
                    pair.setException(ex);
                    pair.setThrower(thrower);
                    pairs.add(pair);
                }
                return pairs;
            }

            private HFNode getExeptionNode(ResultSet rs) throws SQLException {
                HFNode node = new HFNode();
                node.setId(rs.getString("ex_id"));
                node.setName(rs.getString("ex_name"));
                node.setType(NodeType.valueOf(rs.getString("ex_type")));
                return node;
            }
        };
    }

    public SqlQuery<Long> getNumExecutionQuery(final String stepName) {
        return new SqlQuery<Long>() {

            @Override
            protected String getQuery() {
                return "SELECT COUNT(node_id) AS count FROM nodes WHERE node_name = ?";
            }

            @Override
            protected void setParameters(PreparedStatement ps) throws SQLException {
                ps.setString(1, stepName);
            }

            @Override
            protected Long getResults(PreparedStatement ps) throws SQLException, NodeNotFoundException {
                ResultSet rs = ps.getResultSet();
                if (rs.next()) {
                    return rs.getLong("count");
                } else {
                    return 0L;
                }
            }
        };
    }

//    void rollbackTransaction() throws SQLException {
//        try {
//            .rollback();
//        } finally {
//            Closer.close();
//             = null;
//        }
//    }
//
//    void commitTransaction() throws SQLException {
//        try {
//            .commit();
//        } finally {
//            Closer.close();
//             = null;
//        }
//    }
//
//    void startTransaction() throws SQLException {
//         = dataSource.getection();
//        .setAutoCommit(false);
//    }
}
