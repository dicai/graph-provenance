package cs265.hf.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum NodeType {

    START(true, "Start"),
    INTERM(true, "Interm"),
    FINISH(true, "Finish"),
    LEAF(true, "Leaf"),
    VSTART(true, "VStart"),
    VINTERM(true, "VInterm"),
    VFINISH(true, "VFinish"),
    DATA(false, "Data"),
    EXCEPTION(false, "Exception");
    private final boolean isStep;
    final String token;
    private static final Map<String, NodeType> map = new HashMap<String, NodeType>(5);

    static {
        for (NodeType nodeType : NodeType.values()) {
            map.put(nodeType.token, nodeType);
        }
    }

    private NodeType(boolean isStep, String token) {
        this.isStep = isStep;
        this.token = token;
    }

    boolean isStepNode() {
        return isStep;
    }

    boolean isDataNode() {
        return !isStep;
    }

    @Override
    public String toString() {
        return token;
    }

    public static NodeType fromString(String s) {
        return map.get(s);
    }
}
