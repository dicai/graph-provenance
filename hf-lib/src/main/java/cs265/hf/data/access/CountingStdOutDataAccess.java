package cs265.hf.data.access;

import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;

/**
 *
 */
public class CountingStdOutDataAccess extends StdOutDataLoader {

    private final long interval;
    private long edges;
    private long nodes;


    public CountingStdOutDataAccess(long interval) {
        this.interval = interval;
        this.edges = 0;
        this.nodes = 0;
    }

    @Override
    public void saveEdge(HFEdge e) throws NodeNotFoundException {
        edges++;
        if ((edges % interval) == 0) {
            System.out.println(edges + " edges.");
        }
    }

    @Override
    public void saveNode(HFNode n) {
        nodes++;
        if ((nodes % interval) == 0) {
            System.out.println(nodes + " nodes.");
        }
    }



}
