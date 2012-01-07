package cs265.hf.data.access;

import cs265.hf.data.HFNode;

/**
 *
 */
public class NodeNotFoundException extends Exception {

    public NodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public NodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NodeNotFoundException(String message) {
        super(message);
    }

    public NodeNotFoundException(HFNode node) {
        this(node.toString());
    }

    public NodeNotFoundException() {
    }
}
