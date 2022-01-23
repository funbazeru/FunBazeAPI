package me.drkapdor.funbazeapi;

import com.google.gson.JsonParser;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.drkapdor.funbazeapi.api.FunBazeApi;
import me.drkapdor.funbazeapi.database.MySQLDatabase;
import me.drkapdor.funbazeapi.handlers.ConnectionHandler;
import me.drkapdor.funbazeapi.handlers.RolesHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class ApiPlugin extends JavaPlugin {

    private static ApiPlugin instance;
    private static FunBazeApi api;
    private static final JsonParser jsonParser = new JsonParser();
    private static RegionManager regionManager;
    private static MySQLDatabase mySQLDatabase;

    public static File dataFolder;
    private static FileConfiguration configuration;

    public static ApiPlugin getInstance() {
        return instance;
    }

    public static FunBazeApi getApi() {
        return api;
    }

    public static MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
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
        api = new FunBazeApi();
        loadConfiguration();
        init();
    }

    private void loadConfiguration() {
        configuration = getConfig();
        configuration.addDefault("DATABASE_NAME", "funbaze");
        configuration.addDefault("DATABASE_USER", "server");
        configuration.addDefault("DATABASE_PASSWORD", "hardpassword");
        configuration.options().copyDefaults(true);
        saveConfig();
    }

    private void init() {
        connectMySQL();
        createDirectories();
        registerHandlers();
        regionManager = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld("town"));
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
        getServer().getPluginManager().registerEvents(new RolesHandler(), this);
    }

    private void connectMySQL() {
        getLogger().log(Level.INFO, "§aОсуществляется подключение к базе данных...");
        //TODO: Сделать адекватный конфиг для подключения к БД
        mySQLDatabase = new MySQLDatabase(
                configuration.getString("DATABASE_NAME"),
                configuration.getString("DATABASE_USER"),
                configuration.getString("DATABASE_PASSWORD")
        );
        mySQLDatabase.connect();
        String sql = "CREATE TABLE IF NOT EXISTS Players " +
                "(`ID` VARCHAR(16) NOT NULL, " +
                "`Nickname` VARCHAR(16) NOT NULL, " +
                "`Name` VARCHAR(64) NOT NULL, " +
                "`IP` VARCHAR(16) NOT NULL, " +
                "`DiscordID` BIGINT(19) NOT NULL, " +
                "`Access` LONGTEXT NOT NULL, " +
                "`Data` LONGTEXT NOT NULL);";
        mySQLDatabase.execute(sql);
    }

}
