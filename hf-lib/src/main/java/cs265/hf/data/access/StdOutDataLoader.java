package cs265.hf.data.access;

import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import java.io.IOException;

/**
 *
 */
public class StdOutDataLoader implements DataLoader {

    @Override
    public void saveNode(HFNode n) {
        System.out.println(n);
    }

    @Override
    public void saveEdge(HFEdge e) throws NodeNotFoundException {
        System.out.println(e);
    }

    @Override
    public void begin() throws IOException {
    }

    @Override
    public void end() throws IOException {
    }

    @Override
    public void fail() throws IOException {
    }

    @Override
    public void cleanup() throws IOException {
    }

    @Override
    public void init() throws IOException {
    }
}
