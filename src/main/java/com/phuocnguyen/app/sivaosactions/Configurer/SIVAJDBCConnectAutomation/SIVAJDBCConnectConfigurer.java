package com.phuocnguyen.app.sivaosactions.Configurer.SIVAJDBCConnectAutomation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class SIVAJDBCConnectConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(SIVAJDBCConnectConfigurer.class);

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Value("${spring.datasource.driver-class-name}")
    private String DRIVER;

    @Value("${spring.datasource.url}")
    private String JDBC_URL;

    public Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException message) {
            logger.error(message.getMessage(), message);
        }
        return null;
    }

    public String getUSERNAME() {
        return this.USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return this.PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getDRIVER() {
        return this.DRIVER;
    }

    public void setDRIVER(String DRIVER) {
        this.DRIVER = DRIVER;
    }

    public String getJdbcUrl() {
        return this.JDBC_URL;
    }

    public void setJDBC_URL(String JDBC_URL) {
        this.JDBC_URL = JDBC_URL;
    }

    @Override
    public String toString() {
        return "SIVAJDBCConnectAutomationConfigurer{" +
                "USERNAME='" + USERNAME + '\'' +
                ", PASSWORD='" + PASSWORD + '\'' +
                ", DRIVER='" + DRIVER + '\'' +
                ", JDBC_URL='" + JDBC_URL + '\'' +
                '}';
    }
}
