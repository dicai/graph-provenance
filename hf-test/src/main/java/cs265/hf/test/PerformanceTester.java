package cs265.hf.test;

import cs265.hf.data.HFNode;
import cs265.hf.data.access.DataAccess;
import cs265.hf.data.access.NodeNotFoundException;
import cs265.hf.data.timing.Timer;
import cs265.hf.data.timing.TimingSettings;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class PerformanceTester {

    private final DataAccess guineaPig;
    private final Timer timer;

    PerformanceTester(DataAccess guineaPig) {
        this.guineaPig = guineaPig;
        TimingSettings.usePrintingTimer();
        this.timer = TimingSettings.getTimer();
    }

    void testQuery1(List<String> argList) throws IOException, NodeNotFoundException {
        HFNode node = new HFNode();
        for (String id : argList) {
            node.setId(id);
            timer.start();
            guineaPig.getProducerOf(node);
            timer.end();
        }
    }

    void testQuery2Or3(List<String> argList) throws IOException, NodeNotFoundException {
        HFNode node = new HFNode();
        for (String id : argList) {
            node.setId(id);
            timer.start();
            guineaPig.getDependentsOf(node);
            timer.end();
        }
    }

    void testQuery9(List<String> argList) throws IOException, NodeNotFoundException {
        for (String val : argList) {
            timer.start();
            guineaPig.getExceptions();
            timer.end();
        }
    }

    void testQuery10(List<String> argList) throws IOException, NodeNotFoundException {
        for (String val : argList) {
            timer.start();
            guineaPig.getNumExecutions(val);
            timer.end();
        }
    }
}
