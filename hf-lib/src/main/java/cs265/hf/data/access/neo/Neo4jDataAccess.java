package cs265.hf.data.access.neo;

import cs265.hf.data.ExceptionPair;
import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import cs265.hf.data.NodeType;
import cs265.hf.data.access.DataAccess;
import cs265.hf.data.access.NodeNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.EmbeddedGraphDatabase;
//import java.util.HashMap;
import org.neo4j.index.IndexService;
//import org.neo4j.index.impl.AbstractIndex.RelTypes;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.kernel.Traversal;

/**
 *
 * @author dicai
 */
public class Neo4jDataAccess implements DataAccess {

    private final GraphDatabaseService graphDb;
    private final IndexService nodeIndex;

    public Neo4jDataAccess() {
        graphDb = new EmbeddedGraphDatabase(Neo4jSettings.PATH);
        nodeIndex = new LuceneIndexService(graphDb);
    }

    @Override
    public void saveNode(HFNode node) {

        Transaction tx = graphDb.beginTx();
        try {
            Node neoNode = graphDb.createNode();

            // set properties for node
            neoNode.setProperty("id", node.getId());
            neoNode.setProperty("name", node.getName());
            neoNode.setProperty("type", node.getType().toString());
            // create node indices
            nodeIndex.index(neoNode, "id", neoNode.getProperty("id"));
            nodeIndex.index(neoNode, "name", neoNode.getProperty("name"));
            nodeIndex.index(neoNode, "type", neoNode.getProperty("type"));
            tx.success();
        } finally {
            tx.finish();
        }
    }

    @Override
    public void saveEdge(HFEdge edge) throws NodeNotFoundException {
        Transaction tx = graphDb.beginTx();
        try {
            Node toNode =
                    nodeIndex.getSingleNode("id", edge.getToId());
            Node fromNode =
                    nodeIndex.getSingleNode("id", edge.getFromId());
            switch (edge.getType()) {
                case DF:
                    fromNode.createRelationshipTo(toNode, Neo4jRelTypes.DATA_FLOW);
                    break;
                case CF:
                    fromNode.createRelationshipTo(toNode, Neo4jRelTypes.CONTROL_FLOW);
                    break;
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    @Override
    public void close() throws IOException {
        nodeIndex.shutdown();
        graphDb.shutdown();
    }

    @Override
    public HFNode getProducerOf(HFNode node) throws IOException {
        TraversalDescription td =
                Traversal.description().prune(Traversal.pruneAfterDepth(1)).relationships(Neo4jRelTypes.DATA_FLOW, Direction.OUTGOING).filter(Traversal.returnAllButStartNode());

        Node neoNode = nodeIndex.getSingleNode("id", node.getId());

        org.neo4j.graphdb.traversal.Traverser t = td.traverse(neoNode);

        Node producerNode = null;
        for (Node producer : t.nodes()) {
            producerNode = producer;
        }

        HFNode returnNode = new HFNode();
        returnNode.setId(producerNode.getProperty("id").toString());
        returnNode.setName(producerNode.getProperty("name").toString());

        returnNode.setType(NodeType.fromString(producerNode.getProperty("type").toString()));

        return returnNode;
    }

    @Override
    public List<HFNode> getDependentsOf(HFNode dataNode) throws IOException, NodeNotFoundException {

        TraversalDescription td =
                Traversal.description().relationships(Neo4jRelTypes.DATA_FLOW, Direction.INCOMING).
                prune(Traversal.pruneAfterDepth(1)).filter(Traversal.returnAllButStartNode());

        Node neoNode = nodeIndex.getSingleNode("id", dataNode.getId());

        Traverser t = td.traverse(neoNode);

        // store traversed nodes 
        List<HFNode> producerNodes = new LinkedList<HFNode>();

        for (Node producer : t.nodes()) {

            HFNode n = new HFNode();
            n.setId(producer.getProperty("id").toString());
            n.setName(producer.getProperty("name").toString());

            n.setType(NodeType.fromString(producer.getProperty("type").toString()));

            producerNodes.add(n);
        }


        return producerNodes;

    }

    public List<HFNode> findOutputQuery(HFNode stepNode) throws IOException, NodeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Traverse-up Query (n nodes)
     *
     */
    public List<HFNode> traverseUpward(HFNode node, int n) {

        // BFS, incoming data flow, pruned after n nodes
        TraversalDescription td =
                Traversal.description().relationships(Neo4jRelTypes.DATA_FLOW, Direction.INCOMING).
                prune(Traversal.pruneAfterDepth(n)).filter(Traversal.returnAllButStartNode()).
                breadthFirst();

        // convert HFNode -> Neo4j node
        Node neoNode = nodeIndex.getSingleNode("id", node.getId());

        // traverse neoNode with td traversal
        Traverser t = td.traverse(neoNode);

        // store traversed nodes
        List<HFNode> upwardNodes = new LinkedList<HFNode>();

        for (Node producer : t.nodes()) {

            // convert Neo4j node -> HFNode
            HFNode hfnode = new HFNode();
            hfnode.setId(producer.getProperty("id").toString());
            hfnode.setName(producer.getProperty("name").toString());

            hfnode.setType(NodeType.fromString(producer.getProperty("type").toString()));

            upwardNodes.add(hfnode);
        }


        return upwardNodes;

    }

    /*
     * Traverse-down Query (n nodes)
     *
     */
    public List<HFNode> traverseDownward(HFNode node, int n) {
        // BFS, incoming data flow, pruned after n nodes
        TraversalDescription td =
                Traversal.description().relationships(Neo4jRelTypes.DATA_FLOW, Direction.OUTGOING).
                prune(Traversal.pruneAfterDepth(n)).filter(Traversal.returnAllButStartNode()).
                breadthFirst();

        // convert HFNode -> Neo4j node
        Node neoNode = nodeIndex.getSingleNode("id", node.getId());

        // traverse neoNode with td traversal
        Traverser t = td.traverse(neoNode);

        // store traversed nodes
        List<HFNode> upwardNodes = new LinkedList<HFNode>();

        for (Node producer : t.nodes()) {

            // convert Neo4j node -> HFNode
            HFNode hfnode = new HFNode();
            hfnode.setId(producer.getProperty("id").toString());
            hfnode.setName(producer.getProperty("name").toString());

            hfnode.setType(NodeType.fromString(producer.getProperty("type").toString()));

            upwardNodes.add(hfnode);
        }


        return upwardNodes;
    }

    @Override
    public List<ExceptionPair> getExceptions() throws IOException {

        List<ExceptionPair> exceptions = new LinkedList<ExceptionPair>();

        for (Node n : nodeIndex.getNodes("type", NodeType.EXCEPTION.toString())) {
            HFNode exception = new HFNode();
            exception.setId(n.getProperty("id").toString());
            exception.setName(n.getProperty("name").toString());
            exception.setType(NodeType.EXCEPTION);


            ExceptionPair pair = new ExceptionPair();
            pair.setException(exception);
            pair.setThrower(getProducerOf(exception));
            exceptions.add(pair);
        }

        return exceptions;
    }

    @Override
    public Long getNumExecutions(String stepName) throws IOException {
        return (long) nodeIndex.getNodes("name", stepName).size();
    }
}
