package com.blank038.kitpvp.api.gui;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.window.FormWindowModal;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.interfaces.MainGui;
import com.blank038.kitpvp.data.KitData;
import com.blank038.kitpvp.data.PlayerData;

public class KitGui extends FormWindowModal
        implements MainGui {
    private String key;

    public KitGui(KitData kitData) {
        super(KitPvp.getInstance().getString("title.buy", false), KitPvp.getInstance().getString("message.buy.display", false)
                        .replace("%amount%", String.valueOf(kitData.getCount())).replace("%kit%", kitData.getName())
                        .replace("%display%", kitData.getDisplay()), KitPvp.getInstance().getString("message.buy.true-button", false),
                KitPvp.getInstance().getString("message.buy.false-button", false));
        this.key = kitData.getKey();
    }

    @Override
    public void onClick(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseModal) {
            FormResponseModal modal = (FormResponseModal) event.getResponse();
            Player player = event.getPlayer();
            PlayerData data = KitPvp.getDataManager().getPlayerData(player);
            if (modal.getClickedButtonId() == 0) {
                KitData kitData = KitPvp.getInstance().getKitManager().getKitData(key);
                if (data.getMoney() >= kitData.getCount()) {
                    // 购买职业成功
                    KitPvp.getDataManager().getPlayerData(player).delMoney(kitData.getCount());
                    KitPvp.getApi().addKit(player, kitData.getKey());
                    player.sendMessage(KitPvp.getInstance().getString("message.buy.success", true)
                            .replace("%kit%", kitData.getName()).replace("%amount%", String.valueOf(kitData.getCount())));
                } else {
                    player.showFormWindow(this);
                    player.sendMessage(KitPvp.getInstance().getString("message.lack-money", true));
                }
            } else {
                MenuGui menuGui = new MenuGui(data.getChest());
                player.showFormWindow(menuGui);
            }
        }
    }
}