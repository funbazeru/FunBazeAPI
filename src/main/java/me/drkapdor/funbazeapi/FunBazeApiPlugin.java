package me.drkapdor.funbazeapi;

import com.google.gson.JsonParser;
import me.drkapdor.funbazeapi.addon.AddonsCommand;
import me.drkapdor.funbazeapi.api.FunBazeApi;
import me.drkapdor.funbazeapi.database.Database;
import me.drkapdor.funbazeapi.database.DatabaseType;
import me.drkapdor.funbazeapi.database.MySQLDatabase;
import me.drkapdor.funbazeapi.database.SQLiteDatabase;
import me.drkapdor.funbazeapi.handlers.ConnectionHandler;
import me.drkapdor.funbazeapi.handlers.RolesHandler;
import me.drkapdor.funbazeapi.rest.FunBazeRestApi;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Плагин, отвечающий за инициализацию FunBazeAPI
 * @author DrKapdor
 */

public class FunBazeApiPlugin extends JavaPlugin {

    private static FunBazeApiPlugin instance;
    private static FunBazeApi api;
    private static FunBazeRestApi restApi;
    private static final JsonParser jsonParser = new JsonParser();
    private static Database database;

    public static File dataFolder;
    public static File addonsFolder;
    private static FileConfiguration configuration;

    /**
     * Возвращает экземпляр плагина
     * @return Экземпляр плагина
     */

    public static FunBazeApiPlugin getInstance() {
        return instance;
    }

    /**
     * Возвращает экземпляр интерфейса
     * @return Экземпляр интерфейса
     */

    public static FunBazeApi getApi() {
        return api;
    }

    /**
     * Возвращает экземпляр REST API
     * @return Экземпляр REST API
     */

    public static FunBazeRestApi getRestApi() {
        return restApi;
    }

    /**
     * Возвращает текущую базу данных
     * @return База данных
     */

    public static Database getDatabase() {
        return database;
    }

    /**
     * Возвращает JsonParser
     * @return JsonParser
     */

    public static JsonParser getJsonParser() {
        return jsonParser;
    }

    /**
     * Возвращает конфигурацию плагина
     * @return Конфигурация плагина
     */

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfiguration();
        api = new FunBazeApi();
        Bukkit.getServicesManager().register(FunBazeApi.class, api, this, ServicePriority.Normal);
        init();
        api.getAddonsManager().load();
        if (configuration.getBoolean("REST.START")) {
            restApi = new FunBazeRestApi(configuration.getString("REST.HOSTNAME"), configuration.getInt("REST.PORT"), database);
            restApi.getServer().start();
        }
    }

    private void loadConfiguration() {
        configuration = getConfig();
        configuration.addDefault("DATABASE.TYPE", "MySQL");
        configuration.addDefault("DATABASE.NAME", "funbaze");
        configuration.addDefault("DATABASE.USER", "server");
        configuration.addDefault("DATABASE.PASSWORD", "hardpassword");
        configuration.addDefault("REST.ENABLED", true);
        configuration.addDefault("REST.HOSTNAME", "127.0.0.1");
        configuration.addDefault("REST.PORT", 80);
        configuration.options().copyDefaults(true);
        saveConfig();
    }

    private void init() {
        connectDatabase();
        createDirectories();
        registerCommands();
        registerHandlers();
    }

    private void createDirectories() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        dataFolder = new File(getDataFolder() + File.separator + "data");
        if (!dataFolder.exists()) dataFolder.mkdir();

        addonsFolder = new File(getDataFolder() + File.separator + "addons");
        if (!addonsFolder.exists() || !addonsFolder.isDirectory()) addonsFolder.mkdir();
    }

    private void registerCommands() {
        getCommand("addons").setExecutor(new AddonsCommand());
    }

    private void registerHandlers() {
        getServer().getPluginManager().registerEvents(new ConnectionHandler(), this);
        getServer().getPluginManager().registerEvents(new RolesHandler(api.getRolesManager()), this);
    }

    private void connectDatabase() {
        getLogger().log(Level.INFO, "§aОсуществляется подключение к базе данных...");
        DatabaseType type = DatabaseType.valueOf(configuration.getString("DATABASE.TYPE"));
        switch (type) {
            case MySQL: {
                database = new MySQLDatabase(
                        configuration.getString("DATABASE.NAME"),
                        configuration.getString("DATABASE.USER"),
                        configuration.getString("DATABASE.PASSWORD")
                );
                break;
            }
            case SQLite: {
                File file = new File(getDataFolder() + File.separator + "database.db");
                if (!file.exists()) {
                    try {
                        if (file.createNewFile())
                            getLogger().log(Level.INFO, "§aСоздаю локальную базу данных...");
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                database = new SQLiteDatabase(file.getPath());
                break;
            }
        }
        database.connect();
        String sql = "CREATE TABLE IF NOT EXISTS Players " +
                "(`ID` VARCHAR(16) NOT NULL, " +
                "`Nickname` VARCHAR(16) NOT NULL, " +
                "`Name` VARCHAR(64) NOT NULL, " +
                "`IP` VARCHAR(16) NOT NULL, " +
                "`DiscordID` BIGINT(19) NOT NULL, " +
                "`Access` LONGTEXT NOT NULL, " +
                "`Data` LONGTEXT NOT NULL);";
        database.execute(sql);
    }
}
