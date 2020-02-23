package com.blank038.kitpvp.manager;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.data.KitData;
import com.blank038.kitpvp.data.PlayerData;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KitManager {
    private KitPvp main;
    private HashMap<String, KitData> kits = new HashMap<>();

    public KitManager(KitPvp kitPvp) {
        main = kitPvp;
    }

    public void init() {
        File kits = new File(main.getDataFolder(), "kits.yml");
        if (!kits.exists()) main.saveResource(kits.getName());
        loadKitData(kits);
    }

    public boolean hasKit(String kitKey) {
        return kits.containsKey(kitKey);
    }

    public void loadKitData(File file) {
        kits.clear();
        Config config = new Config(file);
        for (String key : config.getKeys(false)) {
            kits.put(key, new KitData(key, config.getSection(key)));
        }
    }

    public KitData getKitData(String key) {
        return kits.get(key);
    }

    public String getKitName(String key) {
        return kits.containsKey(key) ? kits.get(key).getName() : "无";
    }

    public void givePlayerKitItem(Player player, String key) {
        if (kits.containsKey(key)) kits.get(key).givePlayerItem(player);
    }

    public Map<String, KitData> getPlayerNotHasKits(Player player) {
        Map<String, KitData> list = new HashMap<>();
        PlayerData playerData = KitPvp.getDataManager().getPlayerData(player);
        for (Map.Entry<String, KitData> entry : kits.entrySet()) {
            if (!playerData.hasKit(entry.getKey())) list.put(entry.getKey(), entry.getValue());
        }
        return list;
    }
}