package cs265.hf.data.timing;

/**
 *
 */
public class PrintingNanoTimer implements Timer{

    private long startTimestamp = 0;

    public void start() {
        startTimestamp = System.nanoTime();
    }

    public void end() {
        long endTimestamp = System.nanoTime();
        System.out.println(endTimestamp - startTimestamp);
    }
}
