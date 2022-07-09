package me.drkapdor.funbazeapi.addon;

import me.drkapdor.funbazeapi.ApiPlugin;

import java.io.File;
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

    public AddonsManager() {
        addons = new HashMap<>();
    }

    /**
     * Подгружает в память дополнения из директории дополнений
     */

    public void load() {
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
                            addon.init();
                            addons.put(addon.getName(), addon);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException |
                    InstantiationException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Выполняет переподгрузку дополнений
     */

    public void reload() {
        addons.clear();
        load();
    }

    /**
     * Возвращает дополнений по названию
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
}
