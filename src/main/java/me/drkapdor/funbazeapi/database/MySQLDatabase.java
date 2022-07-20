package me.drkapdor.funbazeapi.database;

import me.drkapdor.funbazeapi.FunBazeApiPlugin;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.logging.Level;

/**
 * Класс, реализующий метод подключения к базе данных MySQL
 * @author DrKapdor
 */

public class MySQLDatabase implements Database {

    private final String dbname;
    private final String user;
    private final String password;

    private Connection connection;

    /**
     * Конструктор подключения
     *
     * @param dbname Название базы данных
     * @param user Имя пользователя
     * @param password Пароль пользователя
     */

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

    @Override
    public void connect() {
        try {
            connection = getConnection();
            FunBazeApiPlugin.getInstance().getLogger().log(Level.INFO, "§aПодключение к базе данных успешно установлено!");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true", user, password);
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
            statement.execute(sql);
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
            return statement.executeQuery(sql);
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
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
