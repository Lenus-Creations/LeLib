package org.lenuscreations.lelib.database.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class MySQLColumn {

    private final String name;
    private final String type;
    private final boolean nullable = false;
    private final boolean autoIncrement = false;
    private final boolean primaryKey = false;
    private final boolean unique = false;
    private final String defaultValue = null;

    public String getSQL() {
        String sql = "`" + name + "` " + type;
        if (!nullable) {
            sql += " NOT NULL";
        }
        if (autoIncrement) {
            sql += " AUTO_INCREMENT";
        }
        if (primaryKey) {
            sql += " PRIMARY KEY";
        }
        if (unique) {
            sql += " UNIQUE";
        }
        if (defaultValue != null) {
            sql += " DEFAULT '" + defaultValue + "'";
        }
        return sql;
    }

}
