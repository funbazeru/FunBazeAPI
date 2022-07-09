package me.drkapdor.funbazeapi.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.drkapdor.funbazeapi.database.Database;
import me.drkapdor.funbazeapi.database.MySQLDatabase;
import me.drkapdor.funbazeapi.utility.RestApiUtils;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Интерфейс взаимодействия c сервером посредством HTTP-запросов
 * @author DrKapdor
 */

public class FunBazeRestApi {

    private final RestApiServer server;
    private final Database database;

    /**
     * Конструктор интерфейса
     *
     * @param hostName Адрес, по которому будет доступен интерфейсй
     * @param serverPort Порт, по которому будет доступно подключение
     * @param database Хранилище данных, используемое сервером
     */

    public FunBazeRestApi(String hostName, int serverPort, Database database) {
        server = new RestApiServer(hostName, serverPort);
        this.database = database;
        registerStockHandlers();
    }

    /**
     * Возвращает сервер, отвечающий за обработку HTTP-запросов
     * @return Сервер, отвечающий за обработку HTTP-запросов
     */

    public RestApiServer getServer() {
        return server;
    }

    private void registerStockHandlers() {
        server.registerHandler("/api/user/rpname", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = RestApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
                List<String> nickname = params.get("nick");
                if (nickname != null) {
                    ResultSet resultSet = database.query("SELECT `Name` from Players where `Nickname`='" + nickname.get(0) + "'");
                    try {
                        if (resultSet.next()) {
                            String response = "<html>" +
                                    "<head>" +
                                    "<title>FunBaze API</title>" +
                                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
                                    "</head>" +
                                    "<body>" + resultSet.getString("Name") + "</body>" +
                                    "</html>";
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            OutputStream output = exchange.getResponseBody();
                            output.write(response.getBytes());
                            output.flush();
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        exchange.sendResponseHeaders(500, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(400, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.registerHandler("/api/user/register_date", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = RestApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
                String nickname = new ArrayList<>(params.get("nick")).get(0);
                String format = new ArrayList<>(params.get("format")).get(0);
                if (nickname != null && format != null) {
                    ResultSet resultSet = database.query("SELECT `Data` from Players where `Nickname`='" + nickname + "'");
                    try {
                        if (resultSet.next()) {
                            JsonElement data = new JsonParser().parse(resultSet.getString("Data"));
                            long value = data.getAsJsonObject().get("registerDate").getAsLong();
                            String date = null;
                            switch (format.toLowerCase()) {
                                case "long": {
                                    date = String.valueOf(value);
                                    break;
                                }
                                case "text": {
                                    date = new SimpleDateFormat("d MMMM yyyy HH:mm:ss").format(value);
                                    break;
                                }
                            }
                            if (date != null) {
                                String response = "<html>" +
                                        "<head>" +
                                        "<title>FunBaze API</title>" +
                                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
                                        "</head>" +
                                        "<body>" + date + "</body>" +
                                        "</html>";
                                exchange.sendResponseHeaders(200, response.getBytes().length);
                                OutputStream output = exchange.getResponseBody();
                                output.write(response.getBytes());
                                output.flush();
                            } else {
                                exchange.sendResponseHeaders(400, -1);
                            }
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        exchange.sendResponseHeaders(500, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(400, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.registerHandler("/api/user/status", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = RestApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
                String nickname = new ArrayList<>(params.get("nick")).get(0);
                if (nickname != null) {
                    ResultSet resultSet = database.query("SELECT `Data` from Players where `Nickname`='" + nickname + "'");
                    try {
                        if (resultSet.next()) {
                            JsonElement data = new JsonParser().parse(resultSet.getString("Data"));
                            String value = data.getAsJsonObject().get("status").getAsString();
                            String response = "<html>" +
                                    "<head>" +
                                    "<title>FunBaze API</title>" +
                                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
                                    "</head>" +
                                    "<body>" + value + "</body>" +
                                    "</html>";
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            OutputStream output = exchange.getResponseBody();
                            output.write(response.getBytes());
                            output.flush();
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        exchange.sendResponseHeaders(500, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(400, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.registerHandler("/api/passport/owner", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = RestApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
                List<String> id = params.get("id");
                if (id != null) {
                    ResultSet resultSet = database.query("SELECT `Nickname` from Players where `ID`='" + RestApiUtils.cyrilize(id.get(0)) + "'");
                    try {
                        if (resultSet.next()) {
                            String response = "<html>" +
                                    "<head>" +
                                    "<title>FunBaze API</title>" +
                                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
                                    "</head>" +
                                    "<body>" + resultSet.getString("Nickname") + "</body>" +
                                    "</html>";
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            OutputStream output = exchange.getResponseBody();
                            output.write(response.getBytes());
                            output.flush();
                        } else {
                            exchange.sendResponseHeaders(400, -1);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        exchange.sendResponseHeaders(400, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(500, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.registerHandler("/api/passport/id", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = RestApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
                List<String> owner = params.get("owner");
                if (owner != null) {
                    ResultSet resultSet = database.query("SELECT `ID` from Players where `Nickname`='" + owner.get(0) + "'");
                    try {
                        if (resultSet.next()) {
                            String response = "<html>" +
                                    "<head>" +
                                    "<title>FunBaze API</title>" +
                                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
                                    "</head>" +
                                    "<body>" + resultSet.getString("ID") + "</body>" +
                                    "</html>";
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            OutputStream output = exchange.getResponseBody();
                            output.write(response.getBytes());
                            output.flush();
                        } else {
                            exchange.sendResponseHeaders(404, -1);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        exchange.sendResponseHeaders(500, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(400, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
    }
}
