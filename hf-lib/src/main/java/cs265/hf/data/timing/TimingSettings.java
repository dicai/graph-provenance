package cs265.hf.data.timing;

/**
 *
 */
public class TimingSettings {

    private static Timer timer = new NoopTimer();

    private static class NoopTimer implements Timer {

        @Override
        public void start() {
            //
        }

        @Override
        public void end() {
            //
        }
    }

    public static void usePrintingTimer() {
        timer = new PrintingNanoTimer();
    }

    public static Timer getTimer() {
        return timer;
    }
}
