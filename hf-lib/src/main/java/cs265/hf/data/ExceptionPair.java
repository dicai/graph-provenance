package cs265.hf.data;

/**
 *
 */
public class ExceptionPair {

    private HFNode exception;
    private HFNode thrower;

    public HFNode getException() {
        return exception;
    }

    public void setException(HFNode exception) {
        this.exception = exception;
    }

    public HFNode getThrower() {
        return thrower;
    }

    public void setThrower(HFNode thrower) {
        this.thrower = thrower;
    }

    @Override
    public String toString() {
        return "ExceptionPair{" + "exception=" + exception + "thrower=" + thrower + '}';
    }
}
