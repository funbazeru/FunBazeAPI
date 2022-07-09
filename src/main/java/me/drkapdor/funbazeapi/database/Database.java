package me.drkapdor.funbazeapi.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Интерфейс обращения к базе данных
 * @author DrKapdor
 */

public interface Database {

    /**
     * Подключается к базе данных
     */

    void connect();

    /**
     * Возвращает текущее подключение
     *
     * @return Текущее подключение к базе данных
     * @throws SQLException Ошибка подключение к базе данных
     */

    Connection getConnection() throws SQLException;

    /**
     * Отправляет безответный запрос к базе данных
     *
     * @param sql Запрос в на языке SQL
     * @param args Присущие аргументы
     */

    void execute(String sql, Object... args);

    /**
     * Отправляет запрос на обновление данных
     *
     * @param sql Запрос в на языке SQL
     * @param args Присущие аргументы
     */

    void update(String sql, Object... args);

    /**
     * Отправляет запрос на получение данных
     *
     * @param sql Запрос в на языке SQL
     * @param args Присущие аргументы
     * @return Набор результатов по запросу
     */

    ResultSet query(String sql, Object... args);

}
