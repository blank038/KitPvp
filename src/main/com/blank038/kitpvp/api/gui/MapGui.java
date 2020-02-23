package com.blank038.kitpvp.api.gui;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.gui.component.CustomButton;
import com.blank038.kitpvp.api.interfaces.MainGui;
import com.blank038.kitpvp.data.PlayerData;

import java.util.Map;

public class MapGui extends FormWindowSimple
        implements MainGui {

    public MapGui() {
        super(KitPvp.getInstance().getString("title.map", false), "");
        // 增加返回菜单按钮
        addButton(new CustomButton(KitPvp.getInstance().getString("message.map.back", false), (clicker) -> {
            PlayerData data = KitPvp.getDataManager().getPlayerData(clicker);
            if (data != null) {
                MenuGui menuGui = new MenuGui(data.getChest());
                clicker.showFormWindow(menuGui);
            }
        }));
        // 遍历增加地图按钮
        Map<String, String> maps = KitPvp.getDataManager().getMaps();
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            final String map = entry.getValue();
            final String showName = entry.getKey();
            addButton(new CustomButton(KitPvp.getInstance().getString("message.map.button", false).replace("%map%", showName), (clicker) -> {
                if (KitPvp.getDataManager().hasMap(showName)) {
                    KitPvp.getDataManager().join(clicker, map);
                } else {
                    clicker.sendMessage(KitPvp.getInstance().getString("message.map.deny", true));
                    MapGui gui = new MapGui();
                    clicker.showFormWindow(gui);
                }
            }));
        }
    }

    @Override
    public void onClick(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseSimple) {
            ((CustomButton) getResponse().getClickedButton()).run(event.getPlayer());
        }
    }
}