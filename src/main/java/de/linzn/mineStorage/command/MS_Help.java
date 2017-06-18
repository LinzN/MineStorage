 package de.linzn.mineStorage.command;
 import org.bukkit.command.CommandSender;

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


 public class MS_Help
   extends SYS_Command_API
 {
   
   public MS_Help(CMD_Command_Provider handler, String specialName)
   {
	   
     super(specialName, "/ms " + specialName, true);
     this.task = handler;
   }
   
   
   public boolean execute(final CommandSender sender, final String[] args)
   {
	     if (sender.hasPermission("minestorage.cmd.help"))
     {
	         task.executorServiceCommands.submit(new Runnable() {
	          public void run() 
	          {
	           if (args.length <= 2)
	               if (args.length < 2) {
	               } else if (args[1].toString().equalsIgnoreCase("2")) {
                       sender.sendMessage(SYS_I18n.translate("interfaceHelp2.title1"));
                       sender.sendMessage(SYS_I18n.translate("interfaceHelp2.title2"));
                       sender.sendMessage(SYS_I18n.translate("interfaceHelp2.title3"));
                       sender.sendMessage(SYS_I18n.translate("interfaceHelp2.title4"));
                       sender.sendMessage(SYS_I18n.translate("interfaceHelp2.title5"));
	                   return;
	               } 

                   sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title1"));
                   sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title2"));
                   sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title3"));
                   sender.sendMessage(SYS_I18n.translate("interfaceHelp1.title4"));
	           }
	         });
     } else {
         sender.sendMessage(SYS_I18n.translate("messages.noPermission", new Object[0]));
     }
     return true;
   }
   
 }
