package com.blank038.kitpvp.data;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import com.blank038.kitpvp.KitPvp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {
    // 玩家拥有的职业列表
    private List<String> kitKeys;
    private int money, death, kill;
    private String name;
    private HashMap<Integer, Item> items = new HashMap<>();
    private String select;
    private boolean inGame;
    private int chest;

    public PlayerData(String name) {
        this.name = name;
        File file = new File(KitPvp.getInstance().getDataFolder() + "/Data/", name + ".yml");
        Config config = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                List<String> kits = new ArrayList<>();
                kits.add("default");
                config = new Config(file);
                config.set("kits", kits);
                config.set("kill", 0);
                config.set("death", 0);
                config.set("money", KitPvp.getInstance().getConfig().getInt("default-money"));
                config.set("select", "default");
                config.set("chest", KitPvp.getInstance().getConfig().getInt("chest"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            config = new Config(file);
        }
        // 获取读取后的文件内数据
        kitKeys = new ArrayList<>(config.getStringList("kits"));
        money = config.getInt("money");
        kill = config.getInt("kill");
        death = config.getInt("death");
        select = KitPvp.getInstance().getKitManager().hasKit(config.getString("select"))
                ? config.getString("select") : "default";
        chest = config.getInt("chest");
    }

    public Player getPlayer() {
        return KitPvp.getInstance().getServer().getPlayer(name);
    }

    public void backUp() {
        Player player = getPlayer();
        // 备份玩家背包
        for (Integer i = 0; i < 41; i++) {
            Item item = player.getInventory().getItem(i);
            if (item != null && item.getId() != 0) {
                items.put(i, item.clone());
            }
        }
    }

    public String getSelect() {
        return select;
    }

    public boolean hasKit(String key) {
        return kitKeys.contains(key);
    }

    public int getChest() {
        return chest;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean status) {
        inGame = status;
    }

    public void addKill() {
        kill++;
    }

    public void addDeath() {
        death++;
    }

    public void addMoney(int change) {
        money += change;
    }

    public void addChest(int change) {
        chest += change;
    }

    public void delChest(int change){
        chest -= change;
        chest = chest < 0 ? 0 : chest;
    }

    public void delMoney(int change) {
        money -= change;
        money = money < 0 ? 0 : money;
    }

    public void setSelect(String key) {
        if (KitPvp.getInstance().getKitManager().hasKit(key) && kitKeys.contains(key)) {
            select = key;
        }
    }

    public void addKit(String key) {
        if (!kitKeys.contains(key)) kitKeys.add(key);
    }

    public void end(Player player) {
        if (inGame) {
            // 重置玩家背包
            resetInventory(player);
            // 传送玩家
            Location location = KitPvp.getDataManager().getLocation();
            if (location == null) {
                teleport(player, KitPvp.getInstance().getServer().getLevelByName("world").getSpawnLocation().getLocation());
            } else {
                teleport(player, location);
            }
        }
        inGame = false;
    }

    public List<String> getKits() {
        return kitKeys;
    }

    private void teleport(Player player, Location location) {
        if (!location.getChunk().isLoaded()) {
            try {
                location.getChunk().load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void resetInventory(Player player) {
        if (player != null) {
            player.getInventory().clearAll();
            for (Map.Entry<Integer, Item> entry : items.entrySet()) {
                player.getInventory().setItem(entry.getKey(), entry.getValue());
            }
            items.clear();
        }
    }

    public int getKills() {
        return kill;
    }

    public int getDeath() {
        return death;
    }

    public int getMoney() {
        return money;
    }

    public void save() {
        Config config = new Config();
        config.set("kits", kitKeys);
        config.set("kill", kill);
        config.set("death", death);
        config.set("money", money);
        config.set("select", select);
        config.set("chest", chest);
        config.save(new File(KitPvp.getInstance().getDataFolder() + "/Data/", name + ".yml"));
    }
}