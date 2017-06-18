package de.linzn.mineStorage.command;

import java.util.UUID;

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

public class MS_Account extends SYS_Command_API {

	public MS_Account(CMD_Command_Provider handler, String specialName) {

		super(specialName, "/ms " + specialName, true);
		this.task = handler;
	}

	@Override
	public boolean execute(final CommandSender sender, final String[] args) {
		if (sender.hasPermission("minestorage.cmd.account")) {
			final Player p = (Player) sender;
			task.executorServiceCommands.submit(new Runnable() {
				@Override
				public void run() {
					if (args.length == 1) {
						int DATAExp = MS_Account.this.getDATAExp(p);
						int DBExp = MS_Account.this.getDBUUIDExp(p.getUniqueId().toString());
						sender.sendMessage(SYS_I18n.translate("interfaceAccount.title1"));
						sender.sendMessage(SYS_I18n.translate("interfaceAccount.title2", DATAExp));
						sender.sendMessage(SYS_I18n.translate("interfaceAccount.title3", DBExp));
						return;
					} else if (args.length == 2) {
						if (sender.hasPermission("minestorage.admin.cmd.accountother")) {
							@SuppressWarnings("deprecation")
							UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
							int DBOExp = 0;
							if (MS_Account.this.isDBUUIDSetup(uuid.toString())) {
								DBOExp = MS_Account.this.getDBUUIDExp(uuid.toString());
							}
							sender.sendMessage(SYS_I18n.translate("interfaceAccountOther.title1", args[1]));
							sender.sendMessage(SYS_I18n.translate("interfaceAccountOther.title2", DBOExp));
							return;
						} else {
							sender.sendMessage(SYS_I18n.translate("messages.noPermission", new Object[0]));
							return;
						}
					}
					sender.sendMessage(SYS_I18n.translate("messages.notCorrectType"));
					return;
				}

			});
		} else {
			sender.sendMessage(SYS_I18n.translate("messages.noPermission", new Object[0]));
		}
		return true;
	}

}
