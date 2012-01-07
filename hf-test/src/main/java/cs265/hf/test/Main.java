package cs265.hf.test;

import cs265.hf.data.access.CountingStdOutDataAccess;
import cs265.hf.data.access.DataAccess;
import cs265.hf.data.access.DataLoader;
import cs265.hf.data.access.NodeNotFoundException;
import cs265.hf.data.access.neo.Neo4jDataAccess;
import cs265.hf.data.access.neo.Neo4jDataLoader;
import cs265.hf.data.access.rdf.RdfDataAccess;
import cs265.hf.data.access.rdf.RdfDataLoader;
import cs265.hf.data.access.sql.SqlDataAccess;
import cs265.hf.data.access.sql.SqlDataLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Main {

    public static void main(String[] args)
            throws FileNotFoundException, NodeNotFoundException, IOException {

        try {
            String mode = args[0];
            if (mode.equals("load")) {
                load(args[1].charAt(0), new File(args[2]));
            } else if (mode.equals("test")) {
                test(args[1].charAt(0), Integer.parseInt(args[2]));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.exit(0);
        }
    }

    private static void load(char mode, File file) throws FileNotFoundException, IOException, NodeNotFoundException, SQLException, Exception {
        DataLoader loader = null;
        switch (mode) {
            case 'n':
                loader = new Neo4jDataLoader();
                break;
            case 'j':
                loader = new RdfDataLoader();
                break;
            case 'p':
            default:
                loader = new SqlDataLoader();
                return;
        }
        loadData(loader, file);
    }

    private static void loadData(DataLoader loader, File file) throws FileNotFoundException, IOException, NodeNotFoundException {

        StreamDataImporter importer = new StreamDataImporter(file);
        importer.addLoader(loader);
        importer.addLoader(new CountingStdOutDataAccess(10000L));
        importer.load();
    }

    private static void test(char mode, int test) throws IOException, NodeNotFoundException {
        DataAccess toTest;
        switch (mode) {
            case 'n':
                toTest = new Neo4jDataAccess();
                break;
            case 'j':
                toTest = new RdfDataAccess();
                break;
            case 'p':
            default:
                toTest = new SqlDataAccess();
        }
        try {
            runTests(toTest, test);
        } finally {
            toTest.close();
        }
    }

    private static void runTests(DataAccess guineaPig, int test) throws IOException, NodeNotFoundException {
        List<String> argList = getTestingArgList();
        PerformanceTester tester = new PerformanceTester(guineaPig);
        switch (test) {
            case 1:
                tester.testQuery1(argList);
                break;
            case 2:
                tester.testQuery2Or3(argList);
                break;
            case 3:
                tester.testQuery2Or3(argList);
                break;
            case 9:
                tester.testQuery9(argList);
                break;
            default:
                tester.testQuery10(argList);
                break;

        }
    }

    private static List<String> getTestingArgList() throws IOException {
        BufferedReader reader = getInputReader();
        List<String> argList = new LinkedList<String>();

        String str = reader.readLine();
        while (str != null) {
            argList.add(str);
            str = reader.readLine();

        }
        return argList;
    }

    private static BufferedReader getInputReader() {
        return new BufferedReader(new InputStreamReader(System.in));

    }
}
