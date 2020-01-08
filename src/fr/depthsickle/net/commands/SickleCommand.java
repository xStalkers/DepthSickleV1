package fr.depthsickle.net.commands;

import fr.depthsickle.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SickleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] strings) {
        if (!(commandSender instanceof Player)) {
            if (strings.length == 0) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(" §e/"+ command.getName() +" reload §7- §fReload configuration files.");
                Bukkit.getConsoleSender().sendMessage(" §e/"+ command.getName() +" give [player] §7- §fGive a sickle to a player.");
                Bukkit.getConsoleSender().sendMessage("");
            }

            if (strings.length == 1) {
                if ("reload".equals(strings[0])) {
                    Main.getInstance().reloadConfig();
                    Main.getInstance().reloadLang();
                    Main.getInstance().registersOptions();
                    Main.getInstance().sendReport();
                    Bukkit.getConsoleSender().sendMessage(Main.getInstance().getLangConfiguration().getString("pluginReload").replace("&", "§"));
                } else {
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage(" §e/"+ command.getName() +" reload §7- §fReload configuration files.");
                    Bukkit.getConsoleSender().sendMessage(" §e/"+ command.getName() +" give [player] §7- §fGive a sickle to a player.");
                    Bukkit.getConsoleSender().sendMessage("");
                }
            }

            if (strings.length == 2) {
                if ("give".equals(strings[0])) {
                    Player commandTarget = Bukkit.getPlayer(strings[1]);

                    if (commandTarget == null) {
                        Bukkit.getConsoleSender().sendMessage(Main.getInstance().getLangConfiguration().getString("noPlayer").replace("&", "§"));
                        return true;
                    }

                    this.item(commandTarget);
                    Bukkit.getConsoleSender().sendMessage(Main.getInstance().getLangConfiguration().getString("giveSender").replace("&", "§").replace("%player%", commandTarget.getName()));
                    commandTarget.sendMessage(Main.getInstance().getLangConfiguration().getString("giveTarget").replace("&", "§").replace("%player%", "console"));
                }
            }
            return true;
        }

        if (strings.length == 0) {
            this.help((Player) commandSender, command.getName());
        }

        if (strings.length == 1) {
            if ("reload".equals(strings[0])) {
                if (!commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Command"))) {
                    commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("noPermission").replace("&", "§"));
                    return true;
                }

                Main.getInstance().reloadConfig();
                Main.getInstance().reloadLang();
                Main.getInstance().registersOptions();
                Main.getInstance().sendReport();
                commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("pluginReload").replace("&", "§"));
            } else if ("togglemessage".equals(strings[0])) {
                if (Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).isToggle()) {
                    Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).setToggle(false);
                    commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("disableToggle").replace("&", "§"));
                    return true;
                }

                Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).setToggle(true);
                commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("enableToggle").replace("&", "§"));
            } else if ("mode".equals(strings[0])) {
                commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("showMode").replace("&", "§").replace("%mode%", ""+ Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).getMode()));
            } else {
                this.help((Player) commandSender, command.getName());
            }
        }

        if (strings.length == 2) {
            if ("give".equals(strings[0])) {
                if (!commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Command"))) {
                    commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("noPermission").replace("&", "§"));
                    return true;
                }

                Player commandTarget = Bukkit.getPlayer(strings[1]);

                if (commandTarget == null) {
                    commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("noPlayer").replace("&", "§"));
                    return true;
                }

                this.item(commandTarget);
                commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("giveSender").replace("&", "§").replace("%player%", commandTarget.getName()));
                commandTarget.sendMessage(Main.getInstance().getLangConfiguration().getString("giveTarget").replace("&", "§").replace("%player%", commandSender.getName()));
            } else if ("mode".equals(strings[0])) {
                if ("harvest".equals(strings[1])) {
                    if (!commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Harvest"))) {
                        commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("noPermission").replace("&", "§"));
                        return true;
                    }

                    if (Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).getMode().equals("harvest")) {
                        commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("alreadyMode").replace("&", "§").replace("%mode%", "harvest"));
                        return true;
                    }

                    Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).setMode("harvest");
                    commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("selectMode").replace("&", "§").replace("%mode%", "harvest"));
                } else if ("autosell".equals(strings[1])) {
                    if (!commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Use.Autosell"))) {
                        commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("noPermission").replace("&", "§"));
                        return true;
                    }

                    if (!Main.getInstance().isVault()) {
                        commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("noVault").replace("&", "§"));
                        return true;
                    }

                    if (Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).getMode().equals("autosell")) {
                        commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("alreadyMode").replace("&", "§").replace("%mode%", "autosell"));
                        return true;
                    }

                    Main.getInstance().getAccountManager().getAccount().get(commandSender.getName()).setMode("autosell");
                    commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("selectMode").replace("&", "§").replace("%mode%", "autosell"));
                } else {
                    commandSender.sendMessage(Main.getInstance().getLangConfiguration().getString("errorMode").replace("&", "§"));
                }
            } else {
                this.help((Player) commandSender, command.getName());
            }
        }

        return true;
    }

    private void help(Player player, String console) {
        player.sendMessage("");
        player.sendMessage("§6§m------------------------------------------------------------");
        if (player.hasPermission(Main.getInstance().getConfig().getString("Permission.Command"))) {
            player.sendMessage(" §e/"+ console +" reload §7- §fReload configuration files.");
            player.sendMessage(" §e/"+ console +" give [player] §7- §fGive a sickle to a player.");
        }
        player.sendMessage(" §e/"+ console +" togglemessage §7- §fEnable / disable messages.");
        player.sendMessage(" §e/"+ console +" mode [harvest - autosell] §7- §fChange your selection mode with the sickle.");
        player.sendMessage("§6§m------------------------------------------------------------");
        player.sendMessage("");
    }

    private void item(Player player) {
        ItemStack item = new ItemStack(Material.matchMaterial(Main.getInstance().getConfig().getString("Material.Item")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.getInstance().getConfig().getString("Material.Name").replace("&", "§"));

        List<String> lore = new ArrayList<>();
        for (String line : Main.getInstance().getConfig().getStringList("Material.Lore")) {
            lore.add(line.replace("&", "§"));
        }

        if (Main.getInstance().getConfig().getBoolean("Material.Attribute")) {
            meta.addItemFlags(new ItemFlag[]{ ItemFlag.HIDE_ATTRIBUTES });
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }

}
