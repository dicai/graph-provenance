/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs265.hf.data.access;

import cs265.hf.data.HFEdge;
import cs265.hf.data.ExceptionPair;
import cs265.hf.data.HFNode;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface DataAccess {

    public void saveNode(HFNode node) throws IOException, NodeNotFoundException;

    public void saveEdge(HFEdge edge) throws IOException, NodeNotFoundException;
    
    public HFNode getProducerOf(HFNode node) throws IOException, NodeNotFoundException;

    public List<HFNode> getDependentsOf(HFNode dataNode) throws IOException, NodeNotFoundException;

    public List<ExceptionPair> getExceptions() throws IOException, NodeNotFoundException;

    public Long getNumExecutions(String stepName) throws IOException, NodeNotFoundException;

    public void close() throws IOException;

}
