package com.blank038.kitpvp.api;

import cn.nukkit.Player;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.data.PlayerData;

public class KitPvpAPI {
    private KitPvp main;

    public KitPvpAPI(KitPvp kitPvp) {
        this.main = kitPvp;
    }

    public void addKit(Player player, String key) {
        PlayerData playerData = KitPvp.getDataManager().getPlayerData(player);
        if (player != null && KitPvp.getInstance().getKitManager().hasKit(key)) {
            playerData.addKit(key);
        }
    }
}