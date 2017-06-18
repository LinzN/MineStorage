package de.linzn.mineStorage;

import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Maps;

import de.linzn.mineStorage.command.MS_Account;
import de.linzn.mineStorage.command.MS_Add;
import de.linzn.mineStorage.command.MS_Admin;
import de.linzn.mineStorage.command.MS_EC;
import de.linzn.mineStorage.command.MS_Help;
import de.linzn.mineStorage.command.MS_Pay;
import de.linzn.mineStorage.command.MS_Set;
import de.linzn.mineStorage.command.MS_Take;
import de.linzn.mineStorage.command.MS_WB;
import de.linzn.mineStorage.sys.SYS_Command_API;


/**
 * Copyright:
 * <ul>
 * <li>Autor: Kekshaus</li>
 * <li>2015</li>
 * <li>www.minegaming.de</li>
 * </ul>
 * 
 */


public class CMD_Command_Provider implements CommandExecutor {
    public boolean isInitialized() {
        return initialized;
    }
    private MineStoragePlugin msclass;
    public ThreadPoolExecutor executorServiceCommands;
    public ThreadPoolExecutor executorServiceRegions;

    
    public CMD_Command_Provider(final MineStoragePlugin msclass) {
        this.msclass = msclass;
        executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executorServiceRegions = new ThreadPoolExecutor(1, 1, 120L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public MineStoragePlugin getMSInstance() {
        return this.msclass;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {


            if (sender.hasPermission("minestorage.cmd.help")){
                sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title1"));
                sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title2"));
                sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title3"));
                sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title4"));
        } else {
            sender.sendMessage(SYS_I18n.translate("messages.noPermission", new Object[0]));
        }
        } else if (getCommands().containsKey(args[0])) {
            String command = args[0];
            if (!getCommands().get(command).execute(sender, args)) {
                sender.sendMessage(SYS_I18n.translate("messages.noCommand"));
            }
        } else {
            sender.sendMessage(SYS_I18n.translate("messages.noCommand"));
        }
        return true;
    }


    private TreeMap<String, SYS_Command_API> commands = Maps.newTreeMap();

    public TreeMap<String, SYS_Command_API> getCommands() {
        return commands;
    }

    private boolean initialized = false;

    public void initialize() {
        try {
            this.commands.put("help", new MS_Help(this, "help"));
            this.commands.put("ec", new MS_EC(this, "ec"));
            this.commands.put("wb", new MS_WB(this, "wb"));
            this.commands.put("add", new MS_Add(this, "add"));
            this.commands.put("take", new MS_Take(this, "take"));
            this.commands.put("account", new MS_Account(this, "account"));
            this.commands.put("pay", new MS_Pay(this, "pay"));
            this.commands.put("admin", new MS_Admin(this, "admin"));
            this.commands.put("set", new MS_Set(this, "set"));
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
