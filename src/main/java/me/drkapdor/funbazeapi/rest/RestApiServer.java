package me.drkapdor.funbazeapi.rest;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.drkapdor.funbazeapi.ApiPlugin;

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
        Thread thread = new Thread(() -> {
            server.setExecutor(null);
            server.start();
        });
        thread.start();
        ApiPlugin.getInstance().getLogger().info("§aREST API сервер был успешно запущен!");
    }
}
