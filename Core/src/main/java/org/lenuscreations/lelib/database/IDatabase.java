package org.lenuscreations.lelib.database;

public interface IDatabase<R, P> {

    String getDriverName();

    R get(String query);

    void put(P value);
}
