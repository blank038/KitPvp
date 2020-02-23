package com.blank038.kitpvp.manager;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.gui.MenuGui;
import com.blank038.kitpvp.api.gui.ShopGui;
import com.blank038.kitpvp.data.PlayerData;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private KitPvp main;
    private HashMap<String, PlayerData> dataMap = new HashMap<>();
    private HashMap<String, String> maps = new HashMap<>();
    private Location location;

    public DataManager(KitPvp kitPvp) {
        this.main = kitPvp;
    }

    public void init() {
        maps.clear();
        for (String key : main.getConfig().getSection("map").keySet()) {
            maps.put(key, main.getConfig().getString("map." + key));
        }
        for (String map : maps.values()) {
            if (!KitPvp.getInstance().getServer().isLevelLoaded(map)) {
                KitPvp.getInstance().getServer().loadLevel(map);
            }
        }
        loadLocation();
    }

    public void loadLocation() {
        // 开始判断文件是否存在
        File arena = new File(main.getDataFolder(), "arena.yml");
        // 读取配置
        Config arenaConfig = new Config(arena);
        if (arenaConfig.getKeys(false).contains("end")) {
            setLocation(loadLocation(arenaConfig.getString("end")));
        }
    }

    public boolean hasPlayer(Player player) {
        return dataMap.containsKey(player.getName()) && dataMap.get(player.getName()).isInGame();
    }

    public void openMenu(Player player) {
        PlayerData playerData = getPlayerData(player);
        if (playerData != null) {
            MenuGui form = new MenuGui(playerData.getChest());
            player.showFormWindow(form);
        }
    }

    public void openShop(Player player) {
        PlayerData playerData = getPlayerData(player);
        if (playerData != null) {
            ShopGui form = new ShopGui(player);
            player.showFormWindow(form);
        }
    }

    public Map<String, String> getMaps() {
        return new HashMap<>(maps);
    }

    public boolean hasMap(String key) {
        return maps.containsKey(key);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location.clone();
    }

    public void join(Player player, String world) {
        Level level = randomLevel();
        if ((world == null && level != null) || (world != null)) {
            if (world != null) {
                if (!KitPvp.getInstance().getServer().isLevelLoaded(world)) {
                    KitPvp.getInstance().getServer().loadLevel(world);
                }
                level = KitPvp.getInstance().getServer().getLevelByName(world);
            }
            loadLocation();
            // 校验玩家存档是否存在
            playerJoin(player);
            // 玩家加入职业战争
            getPlayerData(player).backUp();
            player.getInventory().clearAll();
            Location location = level.getSafeSpawn().getLocation();
            level.loadChunk(location.getChunkX(), location.getChunkZ());
            getPlayerData(player).setInGame(true);
            player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            KitPvp.getInstance().getKitManager().givePlayerKitItem(player, getPlayerData(player).getSelect());
        }
    }

    public PlayerData getPlayerData(Player player) {
        return dataMap.get(player.getName());
    }

    public void playerJoin(Player player) {
        if (!dataMap.containsKey(player.getName())) {
            dataMap.put(player.getName(), new PlayerData(player.getName()));
        }
    }

    public void playerQuit(Player player) {
        if (dataMap.containsKey(player.getName())) {
            dataMap.get(player.getName()).end(player);
            dataMap.get(player.getName()).save();
            dataMap.remove(player.getName());
        }
    }

    private Level randomLevel() {
        return main.getServer().getLevelByName(maps.get(maps.keySet().toArray(
                new String[0])[(int) (Math.random() * maps.size())])
        );
    }

    public void resetAll() {
        for (Map.Entry<String, PlayerData> entry : dataMap.entrySet()) {
            entry.getValue().resetInventory(entry.getValue().getPlayer());
        }
    }

    private Location loadLocation(String text) {
        String[] split = text.split("//");
        return new Location(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                Double.parseDouble(split[3]), Double.parseDouble(split[4]), main.getServer().getLevelByName(split[5]));
    }

    public void save() {
        if (location != null && location.getLevel() != null) {
            File arena = new File(main.getDataFolder(), "arena.yml");
            Config arenaConfig = new Config(arena);
            String text = location.getX() + "//" + location.getY() + "//" + location.getZ() + "//" + location.getYaw() + "//" +
                    location.getPitch() + "//" + location.getLevel().getName();
            arenaConfig.set("end", text);
            arenaConfig.save(arena);
        }
        dataMap.entrySet().forEach((entry -> entry.getValue().save()));
    }

    public String formatString(List<String> text) {
        String result = "";
        for (int i = 0; i < text.size(); i++) {
            String msg = text.get(i);
            if ((i + 1 == text.size())) {
                result += msg;
            } else {
                result += msg + "\n";
            }
        }
        return result;
    }
}