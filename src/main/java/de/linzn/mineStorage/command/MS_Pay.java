package de.linzn.mineStorage.command;

import org.bukkit.Bukkit;
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

public class MS_Pay extends SYS_Command_API {

	public MS_Pay(CMD_Command_Provider handler, String specialName) {

		super(specialName, "/ms " + specialName, true);
		this.task = handler;
	}

	@Override
	public boolean execute(final CommandSender sender, final String[] args) {
		if (sender.hasPermission("minestorage.cmd.pay")) {
			final Player from = (Player) sender;
			task.executorServiceCommands.submit(new Runnable() {
				@Override
				public void run() {
					if (args.length == 3) {
						String to = args[1].toLowerCase();
						int amount = 0;
						try {
							amount = Integer.parseInt(args[2]);
						} catch (NumberFormatException ex) {
							sender.sendMessage(SYS_I18n.translate("messages.notNumber"));
							return;
						}
						if (amount <= 0) {
							sender.sendMessage(SYS_I18n.translate("messages.notNegativOrNull"));
							return;
						}
						if (to.equalsIgnoreCase(from.getName().toLowerCase())) {
							sender.sendMessage(SYS_I18n.translate("messages.notPaySelf"));
							return;
						}
						@SuppressWarnings("deprecation")
						String uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString();
						MS_Pay.this.transferDBUUIDExp(from.getUniqueId().toString(), uuid, amount, from, args[1]);
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
