package com.blank038.kitpvp.api.gui;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.gui.component.CustomButton;
import com.blank038.kitpvp.api.interfaces.MainGui;
import com.blank038.kitpvp.data.KitData;

import java.util.HashMap;
import java.util.Map;

public class ShopGui extends FormWindowSimple
        implements MainGui {

    public ShopGui(Player player) {
        super(KitPvp.getInstance().getString("title.shop", false), "");
        Map<String, KitData> kits = new HashMap<>(KitPvp.getInstance().getKitManager().getPlayerNotHasKits(player));
        addButton(new CustomButton(KitPvp.getInstance().getString("message.shop.back", false),
                (clicker) -> KitPvp.getDataManager().openMenu(clicker)));
        for (Map.Entry<String, KitData> entry : kits.entrySet()) {
            final String key = entry.getKey();
            addButton(new CustomButton(KitPvp.getInstance().getString("message.shop.shop-button", false).replace("%kit%", entry.getValue().getName())
                    .replace("%amount%", String.valueOf(entry.getValue().getCount())), (clicker) -> {
                KitGui kitGui = new KitGui(KitPvp.getInstance().getKitManager().getKitData(key));
                clicker.showFormWindow(kitGui);
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
