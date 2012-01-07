package cs265.hf.data.access.neo;

import cs265.hf.data.EdgeType;
import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import cs265.hf.data.access.DataLoader;
import cs265.hf.data.access.NodeNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.neo4j.index.lucene.LuceneIndexBatchInserter;
import org.neo4j.index.lucene.LuceneIndexBatchInserterImpl;
import org.neo4j.kernel.impl.batchinsert.BatchInserter;
import org.neo4j.kernel.impl.batchinsert.BatchInserterImpl;

/**
 *
 */
public class Neo4jDataLoader implements DataLoader {

    private final BatchInserter inserter;
    private final LuceneIndexBatchInserter batchIndex;
    private boolean indexOptimized = false;

    public Neo4jDataLoader() {
        inserter = new BatchInserterImpl(Neo4jSettings.PATH);
        batchIndex = new LuceneIndexBatchInserterImpl(inserter);
    }

    @Override
    public void begin() throws IOException {
        //do nothing
    }

    @Override
    public void saveNode(HFNode node) throws IOException, NodeNotFoundException {
        Map<String, Object> nodeMap = new HashMap<String, Object>(10);
        nodeMap.put("id", node.getId());
        nodeMap.put("name", node.getName());
        nodeMap.put("type", node.getType().toString());

        long neoNode = inserter.createNode(nodeMap);
        batchIndex.index(neoNode, "id", node.getId());
        batchIndex.index(neoNode, "name", node.getName());
        batchIndex.index(neoNode, "type", node.getType().toString());

    }

    @Override
    public void saveEdge(HFEdge edge) throws IOException, NodeNotFoundException {
        if (!indexOptimized) {
            batchIndex.optimize();
        }
        long toNode =
                batchIndex.getSingleNode("id", edge.getToId());
        long fromNode =
                batchIndex.getSingleNode("id", edge.getFromId());
        switch (edge.getType()) {
            case DF:
                inserter.createRelationship(fromNode, toNode, Neo4jRelTypes.DATA_FLOW, null);
                break;
            case CF:
                inserter.createRelationship(fromNode, toNode, Neo4jRelTypes.CONTROL_FLOW, null);
                break;
        }
    }

    @Override
    public void end() throws IOException {
        //do nothing
    }

    @Override
    public void fail() throws IOException {
        //do nothing
    }

    @Override
    public void cleanup() throws IOException {
        batchIndex.shutdown();
        inserter.shutdown();
    }

    @Override
    public void init() throws IOException {
        //do nothing
    }
}
