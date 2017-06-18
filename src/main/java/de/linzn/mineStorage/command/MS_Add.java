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


 public class MS_Add
   extends SYS_Command_API
 {
   
   public MS_Add(CMD_Command_Provider handler, String specialName)
   {
	   
     super(specialName, "/ms " + specialName, true);
     this.task = handler;
   }
   
   
   public boolean execute(final CommandSender sender, final String[] args)
   {
	     if (sender.hasPermission("minestorage.cmd.add"))
     {
	         final Player p = (Player)sender;
	         task.executorServiceCommands.submit(new Runnable() {
	          public void run() 
	          {
	               if (args.length == 2) {
	                   int amount = 0;
	                   try {
	                       amount = Integer.parseInt(args[1]);
	               } catch(NumberFormatException ex) {
	                   sender.sendMessage(SYS_I18n.translate("messages.notNumber"));
	                   return;
	               }
	                   if (amount <= 0) {
	                       sender.sendMessage(SYS_I18n.translate("messages.notNegativOrNull"));
	                       return;
	                   }
	                   MS_Add.this.addDBUUIDExp(p.getUniqueId().toString(), amount, p);
	                   return;
	               }
                   sender.sendMessage(SYS_I18n.translate("messages.notCorrectType"));
	          }
	           
	         });
     } else {
         sender.sendMessage(SYS_I18n.translate("messages.noPermission", new Object[0]));
     }
     return true;
   }
   
 }
