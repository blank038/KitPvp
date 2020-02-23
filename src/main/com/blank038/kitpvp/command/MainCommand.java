package com.blank038.kitpvp.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.blank038.kitpvp.KitPvp;
import com.blank038.kitpvp.api.gui.ChestGui;
import com.blank038.kitpvp.api.gui.InfoGui;
import com.blank038.kitpvp.api.gui.SelectGui;
import com.blank038.kitpvp.data.PlayerData;

public class MainCommand extends Command {
    private KitPvp kitPvp;

    public MainCommand(KitPvp kitPvp) {
        super("kitpvp", "Command for KitPvp.", "职业战争命令帮助", new String[]{"kp"});
        this.kitPvp = kitPvp;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (strings.length == 0) {
            for (String text : kitPvp.getConfig().getStringList("message.help." + (sender.hasPermission("kitpvp.admin") ? "admin" : "default"))) {
                sender.sendMessage(text.replace("&", "§"));
            }
        } else {
            switch (strings[0].toLowerCase()) {
                case "menu":
                    if (sender instanceof Player) KitPvp.getDataManager().openMenu((Player) sender);
                    break;
                case "setend":
                    setLoc(sender);
                    break;
                case "info":
                    info(sender);
                    break;
                case "join":
                    join(sender);
                    break;
                case "leave":
                    quit(sender);
                    break;
                case "pay":
                    add(sender, strings, true);
                    break;
                case "give":
                    add(sender, strings, false);
                    break;
                case "select":
                    select(sender);
                    break;
                case "chest":
                    chest(sender);
                    break;
                case "shop":
                    if (sender instanceof Player) KitPvp.getDataManager().openShop((Player) sender);
                    break;
                case "reload":
                    if (sender.hasPermission("kitpvp.admin")) {
                        kitPvp.loadConfig();
                        sender.sendMessage(kitPvp.getString("message.reload", true));
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public void select(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            SelectGui gui = new SelectGui(player);
            player.showFormWindow(gui);
        }
    }

    public void setLoc(CommandSender sender) {
        if (sender instanceof Player && sender.hasPermission("kitpvp.admin")) {
            KitPvp.getDataManager().setLocation(((Player) sender).getLocation());
            sender.sendMessage(kitPvp.getString("message.set-end", true));
        }
    }

    public void join(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (KitPvp.getDataManager().hasPlayer(player)) {
                player.sendMessage(KitPvp.getInstance().getString("message.in", true));
            } else {
                if (kitPvp.getConfig().getSection("map").keySet().isEmpty()) {
                    player.sendMessage(KitPvp.getInstance().getString("message.empty-maps", true));
                } else {
                    KitPvp.getDataManager().join(player, null);
                }
            }
        }
    }

    public void add(CommandSender sender, String[] args, boolean pay) {
        if (sender.hasPermission("kitpvp.admin")) {
            switch (args.length) {
                case 1:
                    sender.sendMessage(kitPvp.getString("message.wrong-player", true));
                    break;
                case 2:
                    sender.sendMessage(kitPvp.getString("message.wrong-amount", true));
                    break;
                default:
                    Player player = kitPvp.getServer().getPlayer(args[1]);
                    if (player == null || !player.isOnline()) {
                        sender.sendMessage(kitPvp.getString("message.wrong-player", true));
                        break;
                    }
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage(kitPvp.getString("message.wrong-amount", true));
                        break;
                    }
                    if (KitPvp.getDataManager().getPlayerData(player) == null) {
                        sender.sendMessage(kitPvp.getPrefix() + TextFormat.WHITE + "玩家游戏数据异常, 请让对方重新加入游戏!");
                    } else {
                        if (pay) {
                            KitPvp.getDataManager().getPlayerData(player).addMoney(amount);
                            sender.sendMessage(kitPvp.getString("message.pay", true).replace("%player%", player.getName())
                                    .replace("%amount%", String.valueOf(amount)));
                        } else {
                            KitPvp.getDataManager().getPlayerData(player).addChest(amount);
                            sender.sendMessage(kitPvp.getString("message.give", true).replace("%player%", player.getName())
                                    .replace("%amount%", String.valueOf(amount)));
                        }
                    }
                    break;
            }
        }
    }

    public void quit(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (KitPvp.getDataManager().hasPlayer(player)) {
                KitPvp.getDataManager().getPlayerData(player).end(player);
            } else {
                player.sendMessage(kitPvp.getString("message.wrong-game", true));
            }
        }
    }

    public void info(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerData data = KitPvp.getDataManager().getPlayerData(player);
            if (data != null) {
                String contect = KitPvp.getDataManager().formatString(KitPvp.getInstance().getConfig().getStringList("message.info")).replace("&", "§").replace("%kills%", String.valueOf(data.getKills()))
                        .replace("%deaths%", String.valueOf(data.getDeath())).replace("%money%", String.valueOf(data.getMoney()))
                        .replace("%select%", kitPvp.getKitManager().getKitName(data.getSelect()));
                InfoGui infoGui = new InfoGui(contect);
                player.showFormWindow(infoGui);
            }
        }
    }

    public void chest(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerData data = KitPvp.getDataManager().getPlayerData(player);
            if (data != null) {
                ChestGui chestGui = new ChestGui(KitPvp.getInstance().getConfig().getString("message.chest.display")
                        .replace("&", "§"));
                player.showFormWindow(chestGui);
            }
        }
    }
}
