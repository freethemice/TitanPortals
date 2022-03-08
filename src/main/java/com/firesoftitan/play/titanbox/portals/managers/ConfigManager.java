package com.firesoftitan.play.titanbox.portals.managers;

import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import com.firesoftitan.play.titanbox.portals.TitanPortals;

public class ConfigManager {
    private SaveManager configFile;
    private int knockBack = 5;
    private int thorns = 5;
    private int damageAll = 15;
    private int odds = 10;

    public ConfigManager() {
        configFile = new SaveManager(TitanPortals.instants.getName(), "config");
        if (!configFile.contains("settings.odds"))
        {
            configFile.set("settings.odds", 10);
        }
        if (!configFile.contains("settings.damageAll"))
        {
            configFile.set("settings.damageAll", 15);
        }
        if (!configFile.contains("settings.thorns"))
        {
            configFile.set("settings.thorns", 5);
        }
        if (!configFile.contains("settings.knockBack"))
        {
            configFile.set("settings.knockBack", 5);
        }


        this.odds = configFile.getInt("settings.odds");
        this.damageAll = configFile.getInt("settings.damageAll");
        this.thorns = configFile.getInt("settings.thorns");
        this.knockBack = configFile.getInt("settings.knockBack");


        configFile.save();
    }

    public int getOdds() {
        return odds;
    }

    public int getKnockBack() {
        return knockBack;
    }

    public int getThorns() {
        return thorns;
    }

    public int getDamageAll() {
        return damageAll;
    }
}
