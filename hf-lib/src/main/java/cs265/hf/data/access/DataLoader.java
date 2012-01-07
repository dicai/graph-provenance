/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs265.hf.data.access;

import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import java.io.IOException;

/**
 *
 */
public interface DataLoader {

    public void begin() throws IOException;

    public void saveNode(HFNode node) throws IOException, NodeNotFoundException;

    public void saveEdge(HFEdge edge) throws IOException, NodeNotFoundException;

    public void end() throws IOException;

    public void fail() throws IOException;

    public void cleanup() throws IOException;

    public void init() throws IOException;
}
