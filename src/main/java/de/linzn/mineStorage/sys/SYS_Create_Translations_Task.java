package de.linzn.mineStorage.sys;

import de.linzn.mineStorage.MSClass;
import de.linzn.mineStorage.SYS_I18n;


/**
 * Copyright:
 * <ul>
 * <li>Autor: Kekshaus</li>
 * <li>2015</li>
 * <li>www.minegaming.de</li>
 * </ul>
 * 
 */


public class SYS_Create_Translations_Task implements Runnable {

    private MSClass       msclass;
    private SYS_MS_Config msclassConfig;

    public SYS_Create_Translations_Task(MSClass msclass) {
        this.msclass = msclass;
        this.msclassConfig = msclass.getMSClassConfig();
    }

    @Override
    public void run() {
        if (!msclassConfig.loaded) {
            msclass.getServer().getScheduler().runTaskLaterAsynchronously(msclass, this, 20L);
        } else {
            msclass.getServer().getScheduler().runTaskAsynchronously(msclass, new Runnable() {
                @Override
                public void run() {
                    new SYS_I18n(msclass, msclassConfig.locale);
                }
            });
        }
    }

}
