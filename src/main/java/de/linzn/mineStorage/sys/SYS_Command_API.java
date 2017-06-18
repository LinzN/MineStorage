package de.linzn.mineStorage.sys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.linzn.mineStorage.CMD_Command_Provider;
import de.linzn.mineStorage.MineStoragePlugin;
import de.linzn.mineStorage.SYS_I18n;
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


public abstract class SYS_Command_API {

    protected String                       specialName;
    protected String                       usage;
    protected CMD_Command_Provider         task;
    private String                         helpI18n;
    private int getdbexp;
    private int getdataexp;

    public SYS_Command_API(String specialName, String usage, boolean isOnlyPlayerCommand) {
        this.specialName = specialName;
        this.usage = usage;
    }

    public abstract boolean execute(CommandSender sender, String[] args);


    public String getSpecialName() {
        return specialName;
    }


    public String getUsage() {
        return usage;
    }

    public String getHelpI18n() {
        return helpI18n;
    }

    public boolean requiresArgumentsHelpI18n() {
        return false;
    }

    public Object[] getRequiredArgumentsHelpI18n() {
        return new Object[0];
    }

    protected void scheduleSyncTask(CMD_Command_Provider handler, Runnable run) {
        handler.getMSInstance().getServer().getScheduler().scheduleSyncDelayedTask(handler.getMSInstance(), run);
    }
    
    protected void scheduleSyncTask(CMD_Command_Provider handler, Runnable run, long delay) {
        handler.getMSInstance().getServer().getScheduler().scheduleSyncDelayedTask(handler.getMSInstance(), run, delay);
    }

    
    public void newDBUUIDSetup(String uuid){
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try
        {
          Connection conn = manager.getConnection("minestorage");
          PreparedStatement sql = conn.prepareStatement("INSERT INTO MSData (UUID) VALUES ('" + uuid + "');");
          sql.executeUpdate();
          sql.close();
          manager.release("minestorage", conn);
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }  
    }

    public int getDBUUIDExp(String uuid){
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try
        {
          Connection conn = manager.getConnection("minestorage");
          PreparedStatement sql = conn.prepareStatement("SELECT Experience FROM MSData WHERE UUID = '" + uuid + "';");
          ResultSet result = sql.executeQuery();
          if (result.next()){
          getdbexp = result.getInt(1);
          }
          result.close();
          sql.close();
          manager.release("minestorage", conn);
          return getdbexp;
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }  
        return 0;
    }

    public Boolean isDBUUIDSetup(String uuid){
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try
        {
            Connection conn = manager.getConnection("minestorage");
            PreparedStatement sql = conn.prepareStatement("SELECT Experience FROM MSData WHERE UUID = '" + uuid + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()){
                result.close();
                sql.close();
                manager.release("minestorage", conn);
                return true;
            } 
                result.close();
                sql.close();
                manager.release("minestorage", conn);
                return false;
            
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        return null;  
    } 

    public void addDBUUIDExp(String uuid, int amount, Player p){
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try
        {
          if (this.isDBUUIDSetup(uuid)) {
          int DBexp = this.getDBUUIDExp(uuid);
          int DATAexp = this.getDATAExp(p);
          int addDBexp = DBexp + amount;
          if (addDBexp >= (MineStoragePlugin.inst().getMSClassConfig().limit + 1)){
              p.sendMessage(SYS_I18n.translate("messages.limit", MineStoragePlugin.inst().getMSClassConfig().limit));
              return;
          }
          int delDATAexp = DATAexp - amount;
          if (delDATAexp < 0) {
              p.sendMessage(SYS_I18n.translate("messages.notEnoughtEXP"));
              return;
          }
          this.setDATAExp(p, delDATAexp);
          Connection conn = manager.getConnection("minestorage");
          PreparedStatement sql = conn.prepareStatement("UPDATE MSData SET Experience = '" + addDBexp + "' WHERE UUID = '" + uuid + "';");
          sql.executeUpdate();
          sql.close();
          manager.release("minestorage", conn);
          p.sendMessage(SYS_I18n.translate("action.add", amount));
           } else {
               this.newDBUUIDSetup(uuid);
               this.addDBUUIDExp(uuid, amount, p);
           }
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }  
    }
    
    public void takeDBUUIDExp(String uuid, int amount, Player p){
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try
        {
          if (this.isDBUUIDSetup(uuid)) {
          int DBexp = this.getDBUUIDExp(uuid);
          int DATAexp = this.getDATAExp(p);
          int addDBexp = DBexp - amount;
          int setDATAexp = DATAexp + amount;
          if (addDBexp < 0) {
              p.sendMessage(SYS_I18n.translate("messages.notEnoughtEXPDB"));
              return;
          }
          this.setDATAExp(p, setDATAexp);
          Connection conn = manager.getConnection("minestorage");
          PreparedStatement sql = conn.prepareStatement("UPDATE MSData SET Experience = '" + addDBexp + "' WHERE UUID = '" + uuid + "';");
          sql.executeUpdate();
          sql.close();
          manager.release("minestorage", conn);
          p.sendMessage(SYS_I18n.translate("action.take", amount));
           } else {
               this.newDBUUIDSetup(uuid);
               this.takeDBUUIDExp(uuid, amount, p);
           }
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }  
    }
    
    public void setDBUUIDExp(String uuid, int amount, Player sender, String arg){
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try
        {
          if (this.isDBUUIDSetup(uuid)) {
              Connection conn = manager.getConnection("minestorage");
          PreparedStatement sql = conn.prepareStatement("UPDATE MSData SET Experience = '" + amount + "' WHERE UUID = '" + uuid + "';");
          sql.executeUpdate();
          sql.close();
          manager.release("minestorage", conn);
          sender.sendMessage(SYS_I18n.translate("action.set", arg,  amount));
           } else {
               this.newDBUUIDSetup(uuid);
               this.setDBUUIDExp(uuid, amount, sender, arg);
           }
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }  
    }

    public void transferDBUUIDExp(String from, String to, int amount, Player sender, String arg){
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try
        {
          if (this.isDBUUIDSetup(from) && this.isDBUUIDSetup(to)) {
          int DBexpFrom = this.getDBUUIDExp(from);
          int DBexpTo = this.getDBUUIDExp(to);
          int addDBexpTo = DBexpTo + amount;
          int delDBexpFrom = DBexpFrom - amount;
          if (addDBexpTo >= (MineStoragePlugin.inst().getMSClassConfig().limit + 1)){
              sender.sendMessage(SYS_I18n.translate("messages.limitother", arg, MineStoragePlugin.inst().getMSClassConfig().limit));
              return;
          }
          if (delDBexpFrom < 0){
              sender.sendMessage(SYS_I18n.translate("messages.notEnoughtEXPDB"));
              return;
          }
          Connection conn = manager.getConnection("minestorage");
          PreparedStatement sql1 = conn.prepareStatement("UPDATE MSData SET Experience = '" + delDBexpFrom + "' WHERE UUID = '" + from + "';");
          PreparedStatement sql2 = conn.prepareStatement("UPDATE MSData SET Experience = '" + addDBexpTo + "' WHERE UUID = '" + to + "';");
          sql1.executeUpdate();
          sql2.executeUpdate();
          sql1.close();
          sql2.close();
          manager.release("minestorage", conn);
          sender.sendMessage(SYS_I18n.translate("action.pay", arg, amount));
          return;
           } else {
               if (!this.isDBUUIDSetup(from)){
                   this.newDBUUIDSetup(from);
               } else if (!this.isDBUUIDSetup(to)){
                   this.newDBUUIDSetup(to);
               }
               this.transferDBUUIDExp(from, to, amount, sender, arg);
               return;
           }
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }  
    }

    public int getDATAExp(Player p){
       getdataexp = SYS_Set_EXP.getTotalExperience(p);
          return getdataexp;
    }

    public void setDATAExp(Player p, int amount){
        SYS_Set_EXP.setTotalExperience(p, amount);
     }

}
