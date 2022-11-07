package uz.mirzokhidkh.springbootthreads.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import iabs.AnyConnectionManager;
import iabs.DBParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.Connection;


@Configuration
public class HikariDataSourceConfig implements AnyConnectionManager {

    private static String userName;
    private static String userPassword;
    private static String url;

    @Value("${iabs.parameters.url}")
    private String iabsParamsUrl;
    @Value("${hikari.minimum-idle}")
    private int minimumIdle;
    @Value("${maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${hikari.connection-test-query}")
    private String connectionTestQuery;
    @Value("${hikari.pool-name}")
    private String hikariPoolName;
    @Value("${hikari.leak-detection-threshold}")
    private int leakDetectionThreshold;

    @Bean
    @Primary
    public HikariDataSource dataSource() throws Exception {
        DBParameters dbParams = new DBParameters(iabsParamsUrl);
        dbParams.initConnection(this);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("oracle.jdbc.OracleDriver");
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(userPassword);
        hikariConfig.setPoolName(hikariPoolName);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setLeakDetectionThreshold(leakDetectionThreshold);
        hikariConfig.setConnectionTestQuery(connectionTestQuery);

        return new HikariDataSource(hikariConfig);
    }

    @Override
    public Connection getConnection(String s, String s1, String s2) throws Exception {
        return null;
    }

    @Override
    public void initConnection(String userName, String password, String dbUrl) throws Exception {
        this.setUserName(userName);
        this.setUserPassword(password);
        this.setUrl(dbUrl);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        HikariDataSourceConfig.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        HikariDataSourceConfig.userPassword = userPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        HikariDataSourceConfig.url = url;
    }

}
