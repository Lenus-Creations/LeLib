package org.lenuscreations.lelib.database.impl;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.lenuscreations.lelib.database.Credentials;
import org.lenuscreations.lelib.database.IDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySQL implements IDatabase<ResultSet, String> {

    private Connection connection;
    private Credentials credentials;

    @Override
    public String getDriverName() {
        return "MySQL Driver";
    }

    @Override
    public ResultSet get(String var1, String query) {
        this.connect();
        PreparedStatement statement = null;
        ResultSet resultSet;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + var1 + (query == null ? ";" : " WHERE " + query + ";"));
            resultSet = statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (statement != null) {
                try {
                    this.connection.close();
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resultSet;
    }


    @SneakyThrows
    @Override
    public void put(String var1, String value, String where) {
        if (where == null || where.isEmpty()) {
            this.update("INSERT INTO " + var1 + " VALUES (" + value + ")");
            return;
        }

        this.update("UPDATE " + var1 + " SET " + value + " WHERE " + where);
    }

    @Override
    public void remove(String var1, String value) {
        this.update("DELETE FROM " + var1 + " WHERE " + value);
    }

    @Override
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    @SneakyThrows
    private void connect() {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("mysql:jdbc://" + credentials.getAddress() + "/" + credentials.getDatabase(), credentials.getUsername(), credentials.getPassword());
    }

    @SneakyThrows
    public void update(String query) {
        this.connect();
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    @Override
    public String toString() {
        return getDriverName() + "@" + getClass().getName();
    }
}
