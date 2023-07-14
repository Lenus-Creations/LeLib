package org.lenuscreations.lelib.database;

public interface IDatabase<R, P> {

    String getDriverName();

    R get(String var1, String var2);

    void put(String var1, P var2, String var3);

    void remove(String var1, String var2);

    void setCredentials(Credentials credentials);
}
