package cs265.hf.parse;

import cs265.hf.data.HFNode;
import cs265.hf.data.NodeType;
import java.io.InputStream;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LittleJilNodeParser extends Parser<HFNode> {
    private static final Pattern pattern;

    static {
        StringBuilder p = new StringBuilder("^(");
        NodeType[] types = NodeType.values();
        int i;
        for (i = 0; i < types.length - 1; i++) {
            p.append(types[i]).append("|");
        }
        p.append(types[i]).append(")");
        p.append(" (\\S+) (.+)$");
        pattern = Pattern.compile(p.toString());
    }
    public LittleJilNodeParser(InputStream data) {
        super(data);
    }

//    private static NodeType getNodeType(String string) {
//        switch (string.charAt(0)) {
//            case 'L':
//                return NodeType.LEAF;
//            case 'S':
//                return NodeType.START;
//            case 'F':
//                return NodeType.FINISH;
//            case 'I':
//                return NodeType.INTERM;
//            case 'D':
//                return NodeType.DATA;
//            case 'E':
//                return NodeType.EXCEPTION;
//            default:
//                if (string.charAt(1) == 'S') {
//                    return NodeType.VSTART;
//                } else {
//                    return NodeType.VFINISH;
//                }
//        }
//    }

    @Override
    protected HFNode parseT(String s) throws ParseException {
        Matcher m = pattern.matcher(s);

        if (m.matches()) {
            HFNode n = new HFNode();
            n.setType(NodeType.fromString(m.group(1)));
            n.setId(m.group(2));
            n.setName(m.group(3));
            return n;
        } else {
            throw new ParseException("Does not match edge format: " + s, 0);
        }
    }
}
