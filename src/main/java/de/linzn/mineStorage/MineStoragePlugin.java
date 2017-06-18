package de.linzn.mineStorage;

import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import de.linzn.mineStorage.sys.SYS_Create_Commands_Task;
import de.linzn.mineStorage.sys.SYS_Create_Translations_Task;
import de.linzn.mineStorage.sys.SYS_MS_Config;
import de.sql.jdbc.ConnectionFactory;
import de.sql.jdbc.ConnectionHandler;
import de.sql.jdbc.ConnectionManager;

/**
 * Copyright:
 * <ul>
 * <li>Autor: Kekshaus</li>
 * <li>2015</li>
 * <li>www.minegaming.de</li>
 * </ul>
 * 
 */

public class MineStoragePlugin extends JavaPlugin{
    private static MineStoragePlugin inst;
    public PluginDescriptionFile pdf;
    private SYS_MS_Config  rkconfig;
    private CMD_Command_Provider commandHandler;

    public static MineStoragePlugin inst() {
        return inst;
    }

    public SYS_MS_Config getMSClassConfig() {
        return rkconfig;
    }

    @Override
    public void onDisable() {

    }

        @Override
        public void onEnable () {

            PluginDescriptionFile version = this.getDescription();
            pdf = version;
            inst = this;
            this.
                    rkconfig = new SYS_MS_Config(this);
            ConnectionFactory factory = new ConnectionFactory(
                    getConfig().getString("Plugin.database"),
                    getConfig().getString("Plugin.username"),
                    getConfig().getString("Plugin.password"));
            ConnectionManager manager = ConnectionManager.DEFAULT;
            ConnectionHandler handler = manager.getHandler("minestorage", factory);

            try {
                Connection connection = handler.getConnection();
                String sql = "CREATE TABLE IF NOT EXISTS MSData (Id int NOT NULL AUTO_INCREMENT, UUID text, Experience int, PRIMARY KEY (Id));";
                Statement action = connection.createStatement();
                action.executeUpdate(sql);
                action.close();
                handler.release(connection);

            } catch (Exception e) {
                getLogger().warning("Unable to connect to database.");
                e.printStackTrace();
                getLogger().warning("Disable plugin...");
                setEnabled(false);
            }
            getServer().getScheduler().runTaskAsynchronously(this, new SYS_Create_Translations_Task(this));
            commandHandler = new CMD_Command_Provider(this);
            this.getServer().getScheduler().runTaskAsynchronously(this, new SYS_Create_Commands_Task(this, commandHandler));
            getCommand("ms").setExecutor(commandHandler);
        }
        Metrics metrics = new Metrics(this);
    }

