package cs265.hf.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum EdgeType {

    CF("CF"), DF("DF");
    final String token;
    private static final Map<String, EdgeType> map = new HashMap<String, EdgeType>(5);

    static {
        for (EdgeType edgeType : EdgeType.values()) {
            map.put(edgeType.token, edgeType);
        }
    }

    private EdgeType(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }

    public static EdgeType fromString(String s) {
        return map.get(s);
    }
}
