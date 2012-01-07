/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs265.hf.data.access.sql;

import cs265.hf.data.HFNode;
import cs265.hf.data.NodeType;
import cs265.hf.data.access.NodeNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author loren
 */
public abstract class SqlQuery<T> {

    public T execute(Connection conn) throws NodeNotFoundException,SQLException {
        PreparedStatement ps = null;
        try {
            return executeNoErrorHandling(ps, conn);
        } finally {
            Closer.close(ps);
        }
    }

    private T executeNoErrorHandling(PreparedStatement ps, Connection conn) throws SQLException, NodeNotFoundException {
        ps = conn.prepareCall(getQuery());
        setParameters(ps);
        ps.execute();
        return getResults(ps);

    }

    protected List<HFNode> getNodes(ResultSet rs) throws SQLException {
        List<HFNode> nodes = new LinkedList<HFNode>();
        while (rs.next()) {
            nodes.add(getNode(rs));
        }
        return nodes;
    }

    protected HFNode getNode(ResultSet rs) throws SQLException {
        HFNode node = new HFNode();
        node.setId(rs.getString("node_id"));
        node.setName(rs.getString("node_name"));
        node.setType(NodeType.valueOf(rs.getString("node_type")));
        return node;
    }

    protected abstract String getQuery();

    protected abstract void setParameters(PreparedStatement ps) throws SQLException;

    protected abstract T getResults(PreparedStatement ps) throws SQLException, NodeNotFoundException;
}
