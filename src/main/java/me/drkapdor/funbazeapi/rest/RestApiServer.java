package me.drkapdor.funbazeapi.rest;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.drkapdor.funbazeapi.ApiPlugin;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RestApiServer {

    private final int port;
    private HttpServer server;

    public RestApiServer(String hostname, int port) {
        this.port = port;
        try {
            server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public void registerHandler(String key, HttpHandler httpHandler) {
        server.createContext(key, httpHandler);
    }

    public void start() {
        Bukkit.getScheduler().runTask(ApiPlugin.getInstance(), () -> {
            server.setExecutor(null);
            server.start();
        });
        ApiPlugin.getInstance().getLogger().info("§aREST API сервер был успешно запущен!");
    }
}
