package me.drkapdor.funbazeapi;

import com.google.gson.JsonParser;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.drkapdor.funbazeapi.database.MySQLDatabase;
import me.drkapdor.funbazeapi.handlers.ConnectionHandler;
import me.drkapdor.funbazeapi.handlers.RolesHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class ApiPlugin extends JavaPlugin {

    private static final String DATABASE_NAME = "funbaze";
    private static final String DATABASE_USER = "server";
    private static final String DATABASE_PASSWORD = "hardpassword";

    private static ApiPlugin instance;
    private static final JsonParser jsonParser = new JsonParser();
    private static RegionManager regionManager;
    private static MySQLDatabase mySQLDatabase;

    public static File dataFolder;

    @Override
    public void onEnable() {
        instance = this;
        init();
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
        mySQLDatabase = new MySQLDatabase(DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD);
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

    public static ApiPlugin getInstance() {
        return instance;
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
}
