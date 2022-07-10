package me.drkapdor.funbazeapi.addon;

import com.comphenix.protocol.wrappers.Pair;
import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.addon.exception.SameAddonException;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Менеджер дополнений к инфраструктурам проекта
 * @author DrKapdor
 */

public class AddonsManager {

    private final Map<String, FBAddon> addons;
    private final Map<String, Collection<Listener>> listenerMap;

    public AddonsManager() {
        addons = new HashMap<>();
        listenerMap = new HashMap<>();
    }

    /**
     * Подгружает в память дополнения из директории
     */

    public boolean load() {
        for (File file : Objects.requireNonNull(ApiPlugin.addonsFolder.listFiles())) {
            try {
                JarFile jarFile = new JarFile(file.getPath());
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry entry = enumeration.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        URL[] urls = { new URL("jar:file:" + file.getPath() +"!/") };
                        URLClassLoader urlClassLoader = new URLClassLoader(urls, ApiPlugin.class.getClassLoader());
                        Class<?> loadedClass = urlClassLoader.loadClass(entry.getName()
                                .replace(".class", "").replace("/", "."));
                        Object object = loadedClass.newInstance();
                        if (object instanceof FBAddon) {
                            FBAddon addon = (FBAddon) object;
                            if (addons.containsKey(addon.getName()))
                                throw new SameAddonException("Невозможно загрузить несколько дополнений с одинаковым названием: " + addon.getName() + " (Версия " + addon.getVersion() + ", автор " + addon.getAuthor() + ")");
                            addon.init();
                            addons.put(addon.getName(), addon);
                        }
                        urlClassLoader.close();
                    }
                }
            } catch (IOException | ClassNotFoundException | InstantiationException |
                    IllegalAccessException | SameAddonException exception) {
                exception.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Выполняет переподгрузку дополнений
     */

    public boolean reload() {
        addons.clear();
        for (Collection<Listener> listeners : listenerMap.values())
            for (Listener listener : listeners) HandlerList.unregisterAll(listener);
        return load();
    }

    /**
     * Возвращает дополнение по названию
     *
     * @param name Название
     * @return Дополнение
     */

    public FBAddon getAddon(String name) {
        return addons.get(name);
    }

    /**
     * Возвращает все дополнения
     * @return Список дополнений
     */

    public Collection<FBAddon> getAddons() {
        return addons.values();
    }

    /**
     * Зарегестрировать слушатель событий дополнения
     *
     * @param addon Дополнение
     * @param listener Слушатель событий
     */

    public void registerListener(FBAddon addon, Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, ApiPlugin.getInstance());
        if (!listenerMap.containsKey(addon.getName()))
            listenerMap.put(addon.getName(), new ArrayList<>());
        listenerMap.get(addon.getName()).add(listener);
    }
}
