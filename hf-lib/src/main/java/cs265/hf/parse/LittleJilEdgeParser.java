package cs265.hf.parse;

import cs265.hf.data.HFEdge;
import cs265.hf.data.EdgeType;
import java.io.InputStream;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LittleJilEdgeParser extends Parser<HFEdge> {

    private static final Pattern pattern;

    static {
        StringBuilder p = new StringBuilder("^(");
        EdgeType[] types = EdgeType.values();
        int i;
        for (i = 0; i < types.length - 1; i++) {
            p.append(types[i]).append("|");
        }
        p.append(types[i]).append(")");
        p.append(" (\\S+) (\\S+)$");
        pattern = Pattern.compile(p.toString());
    }

    public LittleJilEdgeParser(InputStream data) {
        super(data);
    }

    @Override
    protected HFEdge parseT(String s) throws ParseException {
        Matcher m = pattern.matcher(s);

        if (m.matches()) {
            HFEdge e = new HFEdge();
            e.setType(EdgeType.fromString(m.group(1)));
            e.setFromId(m.group(2));
            e.setToId(m.group(3));
            return e;
        } else {
            throw new ParseException("Does not match edge format: " + s, 0);
        }
    }
}
