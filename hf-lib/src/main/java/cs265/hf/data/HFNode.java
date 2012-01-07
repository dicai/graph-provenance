package cs265.hf.data;

/**
 *
 */
public class HFNode {

    protected String id;
    protected String name;
    protected NodeType type;

    public HFNode() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + "name=" + name + "type=" + type + '}';
    }

}
