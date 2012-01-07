package cs265.hf.test;

import cs265.hf.data.HFEdge;
import cs265.hf.data.HFNode;
import cs265.hf.data.access.DataLoader;
import cs265.hf.data.access.NodeNotFoundException;
import cs265.hf.parse.LittleJilEdgeParser;
import cs265.hf.parse.LittleJilNodeParser;
import cs265.hf.parse.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StreamDataImporter {

    private final Parser<HFNode> pn;
    private final Parser<HFEdge> pe;
    private List<DataLoader> loaders;

    public StreamDataImporter(File fileToLoad) throws FileNotFoundException {
        pn = new LittleJilNodeParser(new FileInputStream(fileToLoad));
        pe = new LittleJilEdgeParser(new FileInputStream(fileToLoad));
        loaders = new ArrayList<DataLoader>(4);
    }

    public void addLoaders(List<DataLoader> loaders) {
        loaders.addAll(loaders);
    }

    public void addLoader(DataLoader dataAccess) {
        loaders.add(dataAccess);
    }

    public void load() throws IOException, NodeNotFoundException {
        for (DataLoader dataLoader : loaders) {
            dataLoader.init();
            dataLoader.begin();
        }
        storeNodes();
        storeEdges();
    }

    private void storeNodes() throws IOException, NodeNotFoundException {
        while (pn.hasNext()) {
            HFNode n = pn.getNext();
            for (DataLoader loader : loaders) {
                loader.saveNode(n);
            }
        }
    }

    private void storeEdges() throws NodeNotFoundException, IOException {
        while (pe.hasNext()) {
            HFEdge e = pe.getNext();
            for (DataLoader loader : loaders) {
                loader.saveEdge(e);
            }
        }
    }
}
