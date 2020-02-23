package com.blank038.kitpvp.api.gui;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.window.FormWindowModal;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.interfaces.MainGui;
import com.blank038.kitpvp.data.KitData;
import com.blank038.kitpvp.data.PlayerData;

import java.util.Map;

public class ChestGui extends FormWindowModal
        implements MainGui {

    public ChestGui(String content) {
        super(KitPvp.getInstance().getString("title.chest", false), content, KitPvp.getInstance().getString("message.chest.true-button", false),
                KitPvp.getInstance().getString("message.chest.false-button", false));
    }

    @Override
    public void onClick(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseModal) {
            FormResponseModal modal = (FormResponseModal) event.getResponse();
            Player player = event.getPlayer();
            PlayerData data = KitPvp.getDataManager().getPlayerData(player);
            if (modal.getClickedButtonId() == 0) {
                int chest = data.getChest();
                if (chest < 1) {
                    player.sendMessage(KitPvp.getInstance().getString("message.chest.lack", true));
                } else {
                    Map<String, KitData> map = KitPvp.getInstance().getKitManager().getPlayerNotHasKits(player);
                    if (map.size() >= 1) {
                        KitPvp.getDataManager().getPlayerData(player).delChest(1);
                        String randomKey = map.keySet().toArray(new String[0])[(int) (0 + Math.random() * (map.size() - 0))];
                        String kitName = map.get(randomKey).getName();
                        setContent(KitPvp.getDataManager().formatString(KitPvp.getInstance().getConfig().getStringList("message.chest.content"))
                                .replace("&", "§").replace("%kit%", kitName));
                        KitPvp.getInstance().getServer().broadcastMessage(KitPvp.getInstance().getString("message.chest.broadcast", true)
                                .replace("%player%", player.getName()).replace("%kit%", kitName));
                        player.showFormWindow(this);
                    } else {
                        player.sendMessage(KitPvp.getInstance().getString("message.chest.deny", true));
                    }
                }
            } else {
                MenuGui menuGui = new MenuGui(data.getChest());
                player.showFormWindow(menuGui);
            }
        }
    }
}