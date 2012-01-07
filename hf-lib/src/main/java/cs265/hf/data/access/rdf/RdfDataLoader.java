package cs265.hf.data.access.rdf;

import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import cs265.hf.data.access.DataLoader;
import cs265.hf.data.access.NodeNotFoundException;
import java.io.IOException;

/**
 *
 */
public class RdfDataLoader implements DataLoader {

    private  RdfDataAccess dataAccess;

    @Override
    public void begin() throws IOException {
        //does nothing
    }

    @Override
    public void saveNode(HFNode node) throws IOException, NodeNotFoundException {
        dataAccess.saveNode(node);
    }

    @Override
    public void saveEdge(HFEdge edge) throws IOException, NodeNotFoundException {
        dataAccess.saveEdge(edge);
    }

    @Override
    public void end() throws IOException {
        //tdbstats.main(argv);
    }

    @Override
    public void fail() throws IOException {
        //does nothing
    }

    @Override
    public void cleanup() throws IOException {
        dataAccess.close();
    }

    @Override
    public void init() throws IOException {
        dataAccess = new RdfDataAccess();
    }

}
