package me.drkapdor.funbazeapi;

import com.google.gson.JsonParser;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ApiPlugin extends JavaPlugin {

    private static ApiPlugin instance;
    private static FunBazeApi api;
    private static FunBazeRestApi restApi;
    private static final JsonParser jsonParser = new JsonParser();
    private static RegionManager regionManager;
    private static Database database;

    public static File dataFolder;
    private static FileConfiguration configuration;

    public static ApiPlugin getInstance() {
        return instance;
    }

    public static FunBazeApi getApi() {
        return api;
    }

    public static FunBazeRestApi getRestApi() {
        return restApi;
    }

    public static Database getDatabase() {
        return database;
    }

    public static RegionManager getRegionManager() {
        return regionManager;
    }

    public static JsonParser getJsonParser() {
        return jsonParser;
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfiguration();
        api = new FunBazeApi();
        init();
        restApi = new FunBazeRestApi(configuration.getString("REST_HOSTNAME"),configuration.getInt("REST_PORT"), database);
        restApi.getServer().start();
        regionManager = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld("town"));
    }

    private void loadConfiguration() {
        configuration = getConfig();
        configuration.addDefault("DATABASE_TYPE", "MySQL");
        configuration.addDefault("DATABASE_NAME", "funbaze");
        configuration.addDefault("DATABASE_USER", "server");
        configuration.addDefault("DATABASE_PASSWORD", "hardpassword");
        configuration.addDefault("REST_HOSTNAME", "127.0.0.1");
        configuration.addDefault("REST_PORT", 80);
        configuration.options().copyDefaults(true);
        saveConfig();
    }

    private void init() {
        connectDatabase();
        createDirectories();
        registerHandlers();
    }

    private void createDirectories() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        dataFolder = new File(getDataFolder() + File.separator + "data");
        if (!dataFolder.exists())
            dataFolder.mkdir();
    }

    private void registerHandlers() {
        getServer().getPluginManager().registerEvents(new ConnectionHandler(), this);
        getServer().getPluginManager().registerEvents(new RolesHandler(api.getRolesManager()), this);
    }

    private void connectDatabase() {
        getLogger().log(Level.INFO, "§aОсуществляется подключение к базе данных...");
        DatabaseType type = DatabaseType.valueOf(configuration.getString("DATABASE_TYPE"));
        switch (type) {
            case MySQL: {
                database = new MySQLDatabase(
                        configuration.getString("DATABASE_NAME"),
                        configuration.getString("DATABASE_USER"),
                        configuration.getString("DATABASE_PASSWORD")
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
