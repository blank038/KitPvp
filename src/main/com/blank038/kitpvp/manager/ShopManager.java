package com.blank038.kitpvp.manager;

import cn.nukkit.utils.Config;
import com.blank038.kitpvp.KitPvp;

import java.io.File;

public class ShopManager {
    private KitPvp main;

    public ShopManager(KitPvp kitPvp) {
        this.main = kitPvp;
    }

    public void init() {
        // 开始判断文件是否存在
        File shop = new File(main.getDataFolder(), "shop.yml");
        if (!shop.exists()) main.saveResource(shop.getName());
        // 读取配置
        Config shopConfig = new Config(shop);
    }
}
