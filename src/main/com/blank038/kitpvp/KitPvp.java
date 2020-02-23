package com.blank038.kitpvp;

import cn.nukkit.plugin.PluginBase;
import com.blank038.kitpvp.api.KitPvpAPI;
import com.blank038.kitpvp.command.MainCommand;
import com.blank038.kitpvp.listener.PlayerListener;
import com.blank038.kitpvp.manager.DataManager;
import com.blank038.kitpvp.manager.KitManager;
import com.blank038.kitpvp.manager.ShopManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitPvp extends PluginBase {
    private static KitPvp instance;
    private static DataManager dataManager;
    private static KitPvpAPI api;
    private KitManager kitManager;
    private ShopManager shopManager;
    private String prefix;
    public List<String> deathMessages = new ArrayList<>();

    public static KitPvp getInstance() {
        return instance;
    }

    public static KitPvpAPI getApi() {
        return api;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        api = new KitPvpAPI(this);
        kitManager = new KitManager(this);
        dataManager = new DataManager(this);
        shopManager = new ShopManager(this);
        loadConfig();
        getServer().getCommandMap().register("KitPvp", new MainCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        getDataManager().resetAll();
        saveAll();
    }

    public void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        saveDefaultConfig();
        reloadConfig();
        File data = new File(getDataFolder(), "Data");
        if (!data.exists()) {
            data.mkdir();
        }
        // 获取提示前缀
        prefix = getConfig().getString("message.prefix").replace("&", "§");
        kitManager.init();
        dataManager.init();
        shopManager.init();
        deathMessages = getConfig().getStringList("message.death-messages");
    }

    public String getPrefix() {
        return prefix;
    }

    public String getString(String key, boolean prefix) {
        return (prefix ? this.prefix : "") + getConfig().getString(key).replace("&", "§");
    }

    public void saveAll() {
        dataManager.save();
    }
}