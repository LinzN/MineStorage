package de.linzn.mineStorage.sys;

import de.linzn.mineStorage.CMD_Command_Provider;
import de.linzn.mineStorage.MineStoragePlugin;


/**
 * Copyright:
 * <ul>
 * <li>Autor: Kekshaus</li>
 * <li>2015</li>
 * <li>www.minegaming.de</li>
 * </ul>
 * 
 */


public class SYS_Create_Commands_Task implements Runnable {

    private MineStoragePlugin       msclass;
    private SYS_MS_Config msclassConfig;

    private CMD_Command_Provider   handler;

    public SYS_Create_Commands_Task(MineStoragePlugin msclass, CMD_Command_Provider handler) {
        this.msclass = msclass;
        this.msclassConfig = msclass.getMSClassConfig();

        this.handler = handler;
    }

    @Override
    public void run() {
        if (!msclassConfig.loaded) {
            msclass.getServer().getScheduler().runTaskLaterAsynchronously(msclass, this, 20L);
        } else {
            msclass.getServer().getScheduler().runTaskAsynchronously(msclass, new Runnable() {
                @Override
                public void run() {
                    if (!handler.isInitialized()) {
                        handler.initialize();
                    }
                }
            });
        }
    }
}
