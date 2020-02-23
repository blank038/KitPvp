package com.blank038.kitpvp.api.gui;

import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.gui.component.CustomButton;
import com.blank038.kitpvp.api.interfaces.MainGui;
import com.blank038.kitpvp.data.PlayerData;

public class MenuGui extends FormWindowSimple
        implements MainGui {

    public MenuGui(int amount) {
        super(KitPvp.getInstance().getString("title.menu", false), "");
        addButton(new CustomButton(KitPvp.getInstance().getString("message.button.join", false), (clicker) -> {
            PlayerData data = KitPvp.getDataManager().getPlayerData(clicker);
            if (data != null) clicker.showFormWindow(new MapGui());
        }));
        for (String text : new String[]{"leave", "shop", "chest", "select", "info"}) {
            addButton(new CustomButton(KitPvp.getInstance().getString("message.button." + text, false).replace("%amount%", String.valueOf(amount)),
                    (clicker) -> KitPvp.getInstance().getServer().dispatchCommand(clicker, "kitpvp " + text)));
        }
    }

    @Override
    public void onClick(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseSimple) {
            ((CustomButton) getResponse().getClickedButton()).run(event.getPlayer());
        }
    }
}