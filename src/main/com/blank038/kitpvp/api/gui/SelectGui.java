package com.blank038.kitpvp.api.gui;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.gui.component.CustomButton;
import com.blank038.kitpvp.api.interfaces.MainGui;
import com.blank038.kitpvp.data.KitData;

import java.util.ArrayList;
import java.util.List;

public class SelectGui extends FormWindowSimple
        implements MainGui {

    public SelectGui(Player player) {
        super(KitPvp.getInstance().getString("title.select", false), "");
        List<String> kits = new ArrayList<>(KitPvp.getDataManager().getPlayerData(player).getKits());
        for (final String key : kits) {
            KitData kitData = KitPvp.getInstance().getKitManager().getKitData(key);
            if (kitData != null) {
                final boolean allow = kitData.allowUse(player);
                final String kitName = kitData.getName();
                String status = allow ? KitPvp.getInstance().getString("message.select.status.allow", false) :
                        KitPvp.getInstance().getString("message.select.status.deny", false);
                addButton(new CustomButton(KitPvp.getInstance().getString("message.select.button", false)
                        .replace("%kit%", kitName).replace("%amount%", String.valueOf(kitData.getCount()))
                        .replace("%status%", status), (clicker) -> {
                    if (allow) {
                        KitPvp.getDataManager().getPlayerData(clicker).setSelect(key);
                        clicker.sendMessage(KitPvp.getInstance().getString("message.select.success", true)
                                .replace("%kit%", kitName));
                    } else {
                        clicker.sendMessage(KitPvp.getInstance().getString("message.select.no-permission", true));
                        SelectGui gui = new SelectGui(clicker);
                        clicker.showFormWindow(gui);
                    }
                }));
            }
        }
    }

    @Override
    public void onClick(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseSimple) {
            ((CustomButton) getResponse().getClickedButton()).run(event.getPlayer());
        }
    }
}