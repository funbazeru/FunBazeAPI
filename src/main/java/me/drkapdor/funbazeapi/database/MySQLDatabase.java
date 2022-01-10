package me.drkapdor.funbazeapi.database;

import me.drkapdor.funbazeapi.ApiPlugin;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.logging.Level;

public class MySQLDatabase {

    private final String dbname;
    private final String user;
    private final String password;

    private Connection connection;

    public MySQLDatabase(String dbname, String user, String password) {
        this.dbname = dbname;
        this.user = user;
        this.password = password;
        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public void connect() {
        try {
            connection = getConnection();
            ApiPlugin.getInstance().getLogger().log(Level.INFO, "§aПодключение к базе данных успешно установлено!");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true", user, password);
        //useUnicode=true&characterEncoding=UTF-8&
        return connection;
    }

    public void execute(String sql, Object... args) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            int i = 1;
            for (Object arg : args) {
                statement.setObject(i, arg);
                i++;
            }
            statement.execute(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public ResultSet query(String sql, Object... args) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            int i = 1;
            for (Object arg : args) {
                statement.setObject(i, arg);
                i++;
            }
            return statement.executeQuery(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void update(String sql, Object... args)  {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            int i = 1;
            for (Object arg : args) {
                statement.setObject(i, arg);
                i++;
            }
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
