package org.lenuscreations.lelib.database;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLDatabase implements SQLDatabase {

    private final Connection connection;

    @SneakyThrows
    public MySQLDatabase(String host, int port, String database, String username, String password) {
        Class.forName("com.mysql.jdbc.Driver");

        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
