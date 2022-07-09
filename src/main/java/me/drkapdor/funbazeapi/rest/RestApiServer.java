package me.drkapdor.funbazeapi.rest;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.drkapdor.funbazeapi.ApiPlugin;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Сервер, отвечающий за обработку HTTP-запросы
 * @author DrKapdor
 */

public class RestApiServer {

    private final int port;
    private HttpServer server;

    /**
     * Конструктор сервера
     *
     * @param hostname Адрес, по которому сервер доступен
     * @param port Порт, к которому будут осуществляться подключение
     */

    public RestApiServer(String hostname, int port) {
        this.port = port;
        try {
            server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Возвращает порт, к которому осуществляются подключение
     * @return Порт, к которому осуществляются подключение
     */

    public int getPort() {
        return port;
    }

    /**
     * Регистрирует обработчик HTTP-запросов к серверу
     * @param key Ключ к запрашиваемому сервису
     * @param httpHandler Обработчик HTTP-запросов
     */

    public void registerHandler(String key, HttpHandler httpHandler) {
        server.createContext(key, httpHandler);
    }

    /**
     * Начинает работу сервера, отвечающего за обработку HTTP-запросов
     */

    public void start() {
        Bukkit.getScheduler().runTask(ApiPlugin.getInstance(), () -> {
            server.setExecutor(null);
            server.start();
        });
        ApiPlugin.getInstance().getLogger().info("§aREST API сервер был успешно запущен!");
    }
}
