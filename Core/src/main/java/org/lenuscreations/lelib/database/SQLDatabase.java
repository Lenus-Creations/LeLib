package org.lenuscreations.lelib.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface SQLDatabase {

    Connection getConnection();

    default void close() {
        try {
            getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default boolean execute(String query) {
        try {
            return statement(query).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    default PreparedStatement statement(String query) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            return statement;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
