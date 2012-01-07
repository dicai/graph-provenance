package cs265.hf.data.access.sql;

import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author loren
 */
public class DataSourceFactory {

    private static final String SERVER = System.getProperty("hf.sql.host", "localhost");
    private static final String DATABASE = System.getProperty("hf.sql.name", "hf");
    private static final String USER = System.getProperty("hf.sql.user", "hf");
    private static final String PASSWORD = System.getProperty("hf.sql.pass", "hf");
    private static final int MAX_CONN = 1;

    static DataSource getDataSource() {
        PGPoolingDataSource ds = new PGPoolingDataSource();
        ds.setServerName(SERVER);
        ds.setDataSourceName(DATABASE);
        ds.setDatabaseName(DATABASE);
        ds.setUser(USER);
        ds.setPassword(PASSWORD);
        ds.setMaxConnections(MAX_CONN);
        return ds;

    }
}
