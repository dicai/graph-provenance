package cs265.hf.data.access.neo;

import org.neo4j.graphdb.RelationshipType;

public enum Neo4jRelTypes implements RelationshipType {

    DATA_FLOW, CONTROL_FLOW
}
