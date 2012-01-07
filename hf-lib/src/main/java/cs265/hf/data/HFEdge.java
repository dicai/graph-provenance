package cs265.hf.data;

public class HFEdge {

    private EdgeType type;
    private String toId;
    private String fromId;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public EdgeType getType() {
        return type;
    }

    public void setType(EdgeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Edge{" + "type=" + type + "toId=" + toId + "fromId=" + fromId + '}';
    }
}
