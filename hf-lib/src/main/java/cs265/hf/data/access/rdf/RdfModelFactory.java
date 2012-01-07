package cs265.hf.data.access.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

/**
 *
 */
public class RdfModelFactory {

    private static final String DIRECTORY = System.getProperty("hf.jena.root", "jena/default");
    private static final Model MODEL = TDBFactory.createModel(DIRECTORY);

    public static Model getModel() {
        return MODEL;
    }
}
