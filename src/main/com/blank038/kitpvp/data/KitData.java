package com.blank038.kitpvp.data;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.ConfigSection;

import java.util.ArrayList;
import java.util.List;

public class KitData {
    private List<Item> items = new ArrayList<>();
    private String name;
    private String display, permission, key;
    private int count;

    public KitData(String key, ConfigSection section) {
        this.name = section.getString("name").replace("&", "§");
        display = section.getString("display").replace("&", "§");
        permission = section.getString("permission");
        count = section.getInt("count");
        this.key = key;
        loadItem(section.getStringList("items"));
    }

    public void loadItem(List<String> texts) {
        for (String text : texts) {
            items.add(formatItem(text));
        }
    }

    public String getKey() {
        return key;
    }

    public boolean allowUse(Player player) {
        return player.hasPermission(permission);
    }

    public String getDisplay() {
        return display;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public Item formatItem(String text) {
        int id = 1, amount = 1, data = 0;
        String name = null;
        String[] lore = new String[0];
        String[] split = text.split("#");
        try {
            for (String i : split) {
                String[] strings = i.split("=");
                if (strings.length > 1) {
                    switch (strings[0].toLowerCase()) {
                        case "id":
                            id = Integer.parseInt(strings[1]);
                            break;
                        case "name":
                            name = strings[1].replace("&", "§");
                            break;
                        case "amount":
                            amount = Integer.parseInt(strings[1]);
                            break;
                        case "data":
                            data = Integer.parseInt(strings[1]);
                            break;
                        case "lore":
                            lore = splitToLore(strings[1]);
                            break;
                    }
                }
            }
        } catch (Exception e) {
        }
        Item item = new Item(id, data, amount);
        if (name != null) item.setCustomName(name);
        item.setLore(lore);
        return item;
    }

    private String[] splitToLore(String text) {
        return text.contains("//") ? formatLore(text.split("//")) : new String[]{text.replace("&", "§")};
    }

    public void givePlayerItem(Player player) {
        player.getInventory().addItem(items.toArray(new Item[0]));
    }

    private String[] formatLore(String[] lores) {
        String[] lore = new String[lores.length];
        for (int i = 0; i < lores.length; i++) {
            lore[i] = (lores[i].replace("&", "§"));
        }
        return lore;
    }
}