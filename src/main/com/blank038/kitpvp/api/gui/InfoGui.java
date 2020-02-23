package com.blank038.kitpvp.api.gui;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.window.FormWindowModal;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.interfaces.MainGui;
import com.blank038.kitpvp.data.PlayerData;

public class InfoGui extends FormWindowModal
        implements MainGui {

    public InfoGui(String content) {
        super(KitPvp.getInstance().getString("title.info", false), content, KitPvp.getInstance().getString("message.info-gui.true-button", false),
                KitPvp.getInstance().getString("message.info-gui.false-button", false));
    }

    @Override
    public void onClick(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseModal) {
            FormResponseModal modal = (FormResponseModal) event.getResponse();
            if (modal.getClickedButtonId() == 0) {
                Player player = event.getPlayer();
                PlayerData data = KitPvp.getDataManager().getPlayerData(player);
                MenuGui menuGui = new MenuGui(data.getChest());
                player.showFormWindow(menuGui);
            }
        }
    }
}