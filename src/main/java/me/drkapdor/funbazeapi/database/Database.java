package me.drkapdor.funbazeapi.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database {

    void connect();

    Connection getConnection() throws SQLException;

    void execute(String sql, Object... args);

    void update(String sql, Object... args);

    ResultSet query(String sql, Object... args);

}
