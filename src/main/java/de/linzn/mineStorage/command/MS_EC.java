 package de.linzn.mineStorage.command;
 import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.linzn.mineStorage.CMD_Command_Provider;
import de.linzn.mineStorage.SYS_I18n;
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


 public class MS_EC
   extends SYS_Command_API
 {
   
   public MS_EC(CMD_Command_Provider handler, String specialName)
   {
	   
     super(specialName, "/ms " + specialName, true);
     this.task = handler;
   }
   
   
   public boolean execute(final CommandSender sender, final String[] args)
   {
       final Player p = (Player)sender;
	     if (sender.hasPermission("minestorage.tool.cmd.ec"))
     {
	         task.executorServiceCommands.submit(new Runnable() {
	          public void run() 
	          {
               p.openInventory(p.getEnderChest());
	           }
	         });
     } else {
         sender.sendMessage(SYS_I18n.translate("messages.noPermission", new Object[0]));
     }
     return true;
   }
   
 }
