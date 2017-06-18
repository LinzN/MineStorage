package de.linzn.mineStorage.sys;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import de.linzn.mineStorage.MSClass;


/**
 * Copyright:
 * <ul>
 * <li>Autor: Kekshaus</li>
 * <li>2015</li>
 * <li>www.minegaming.de</li>
 * </ul>
 * 
 */


public class SYS_MS_Config {

    private FileConfiguration  config;

    public String              locale                = "deDE";
    public int                 limit                = 6000;
    public boolean             loaded                = false;
    

    public SYS_MS_Config(final MSClass msclass) {


        msclass.getServer().getScheduler().runTaskAsynchronously(msclass, new Runnable() {
            public void run() {
                msclass.saveDefaultConfig();

                config = msclass.getConfig();
                loadConfiguration();

                loaded = true;

                msclass.saveDefaultConfig();
            }
        });
    }
    private boolean existsPath(final String path) {
        return existsPath(path, "");
    }

    private boolean existsPath(final String pPath, final String path) {
        String pathPrefix = "";
        if (!path.isEmpty())
            pathPrefix = path + ".";
        if (config.isConfigurationSection(path)) {
            final Set<String> key = config.getConfigurationSection(path).getKeys(false);
            if (key.size() > 0) {
                final String[] paths = key.toArray(new String[0]);
                for (final String thePath : paths) {
                    if (existsPath(pPath, pathPrefix + thePath)) {
                        return true;
                    }
                }
            }
        } else {
            return pPath.equalsIgnoreCase(path);
        }
        return false;
    }

    private <T> T get(String path, T def) {
        try {
            if (!existsPath(path)) {
                config.set(path, def);
            }

            @SuppressWarnings("unchecked")
            T flag = (T) config.get(path);
            return flag;
        } catch (Exception e) {
            return def;
        }
    }

    private void loadConfiguration() {
        locale = get("Plugin.locale", "deDE");
        limit = get("Plugin.limit", 6000);
        
        
    }
}
