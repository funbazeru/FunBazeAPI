package me.drkapdor.funbazeapi.database;

import me.drkapdor.funbazeapi.ApiPlugin;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.logging.Level;

/**
 * Класс, реализующий метод подключения к базе данных SQLite
 * @author DrKapdor
 */

public class SQLiteDatabase implements Database {

    private final String path;
    private Connection connection;

    /**
     * Конструктор подключения
     * @param path Путь к файлу формата .db
     */

    public SQLiteDatabase(String path) {
        this.path = path;
        try {
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void connect() {
        try {
            connection = getConnection();
            ApiPlugin.getInstance().getLogger().log(Level.INFO, "§aПодключение к базе данных успешно установлено!");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        //useUnicode=true&characterEncoding=UTF-8&
        return connection;
    }

    @Override
    public void execute(String sql, Object... args) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            int i = 1;
            for (Object arg : args) {
                statement.setObject(i, arg);
                i++;
            }
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public ResultSet query(String sql, Object... args) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            int i = 1;
            for (Object arg : args) {
                statement.setObject(i, arg);
                i++;
            }
            return statement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(String sql, Object... args)  {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            int i = 1;
            for (Object arg : args) {
                statement.setObject(i, arg);
                i++;
            }
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
