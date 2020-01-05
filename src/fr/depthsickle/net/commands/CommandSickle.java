package fr.depthsickle.net.commands;

import fr.depthsickle.net.Main;
import fr.depthsickle.net.helpers.PluginsHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSickle implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (!(commandSender instanceof Player)) {
            if (args.length == 0) {
                Bukkit.getConsoleSender().sendMessage("§cSyntaxe : /depthsickle [reload - give].");
            }

            if (args.length == 1) {
                if ("reload".equals(args[0])) {
                    Main.getInstance().reloadConfig();
                    Main.getInstance().reloadMessageFiles();
                    Main.getInstance().registersOptions();
                    Bukkit.getConsoleSender().sendMessage(Main.getInstance().getFileMessageConfiguration().getString("pluginReload").replace("&", "§"));
                } else {
                    Bukkit.getConsoleSender().sendMessage("§cSyntaxe : /depthsickle [reload - give].");
                }
            }

            if (args.length == 2) {
                if ("give".equals(args[0])) {
                    Player commandTarget = Bukkit.getPlayer(args[1]);

                    if (commandTarget == null) {
                        Bukkit.getConsoleSender().sendMessage(Main.getInstance().getFileMessageConfiguration().getString("noPlayer").replace("&", "§"));
                        return true;
                    }

                    Main.getInstance().getPluginsHelper().item(commandTarget);
                    Bukkit.getConsoleSender().sendMessage(Main.getInstance().getFileMessageConfiguration().getString("giveSender").replace("&", "§").replace("%player%", commandTarget.getName()));
                    commandTarget.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("giveTarget").replace("&", "§").replace("%player%", "console"));
                } else {
                    Bukkit.getConsoleSender().sendMessage("§cSyntaxe : /depthsickle [reload - give].");
                }
            }

            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage("");
            commandSender.sendMessage("§6§m------------------------------------------------------------");
            if (commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Commands"))) {
                commandSender.sendMessage(" §e/depthsickle reload §7- §fReload configuration files.");
                commandSender.sendMessage(" §e/depthsickle give [player] §7- §fGive a sickle to a player.");
            }
            commandSender.sendMessage(" §e/depthsickle togglemessage §7- §fEnable / disable messages.");
            commandSender.sendMessage(" §e/depthsickle statistic §7- §fKnow the number of times you have used the sickle.");
            commandSender.sendMessage("§6§m------------------------------------------------------------");
            commandSender.sendMessage("");
        }

        if (args.length == 1) {
            if ("reload".equals(args[0])) {
                if (!commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Commands"))) {
                    commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("noAccessPermission").replace("&", "§"));
                    return true;
                }

                Main.getInstance().reloadConfig();
                Main.getInstance().reloadMessageFiles();
                Main.getInstance().registersOptions();
                commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("pluginReload").replace("&", "§"));
            } else if ("togglemessage".equals(args[0])) {
                if (Main.getInstance().getPlayersManager().getAccount().get(commandSender.getName()).isToggle()) {
                    Main.getInstance().getPlayersManager().getAccount().get(commandSender.getName()).setToggle(false);
                    commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("disableMessage").replace("&", "§"));
                    return true;
                }

                Main.getInstance().getPlayersManager().getAccount().get(commandSender.getName()).setToggle(true);
                commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("enableMessage").replace("&", "§"));
            } else if ("statistic".equals(args[0]) || "stats".equals(args[0])) {
                commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("playerStatistic").replace("%number%", ""+ Main.getInstance().getPlayersManager().getAccount().get(commandSender.getName()).getUsed()).replace("&", "§"));
            } else {
                commandSender.sendMessage("");
                commandSender.sendMessage("§6§m------------------------------------------------------------");
                if (commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Commands"))) {
                    commandSender.sendMessage(" §e/depthsickle reload §7- §fReload configuration files.");
                    commandSender.sendMessage(" §e/depthsickle give [player] §7- §fGive a sickle to a player.");
                    commandSender.sendMessage(" §e/depthsickle mod check [player] [mod] §7- §fGive informations in to a player's mod.");
                    commandSender.sendMessage(" §e/depthsickle mod set [player] [mod] §7- §fChange mod in to a player.");
                }
                commandSender.sendMessage(" §e/depthsickle togglemessage §7- §fEnable / disable messages.");
                commandSender.sendMessage(" §e/depthsickle statistic §7- §fKnow the number of times you have used the sickle.");
                commandSender.sendMessage("§6§m------------------------------------------------------------");
                commandSender.sendMessage("");
            }
        }

        if (args.length == 2) {
            if ("give".equals(args[0])) {
                if (!commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Commands"))) {
                    commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("noAccessPermission").replace("&", "§"));
                    return true;
                }

                Player commandTarget = Bukkit.getPlayer(args[1]);


                if (commandTarget == null) {
                    commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("noPlayer").replace("&", "§"));
                    return true;
                }

                if (commandTarget.equals(commandSender)) {
                    Main.getInstance().getPluginsHelper().item((Player) commandSender);
                    commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("giveSender").replace("&", "§").replace("%player%", commandSender.getName()));
                    return true;
                }

                Main.getInstance().getPluginsHelper().item(commandTarget);
                commandSender.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("giveSender").replace("&", "§").replace("%player%", commandTarget.getName()));
                commandTarget.sendMessage(Main.getInstance().getFileMessageConfiguration().getString("giveTarget").replace("&", "§").replace("%player%", commandSender.getName()));
            } else {
                commandSender.sendMessage("");
                commandSender.sendMessage("§6§m------------------------------------------------------------");
                if (commandSender.hasPermission(Main.getInstance().getConfig().getString("Permission.Commands"))) {
                    commandSender.sendMessage(" §e/depthsickle reload §7- §fReload configuration files.");
                    commandSender.sendMessage(" §e/depthsickle give [player] §7- §fGive a sickle to a player.");
                }
                commandSender.sendMessage(" §e/depthsickle togglemessage §7- §fEnable / disable messages.");
                commandSender.sendMessage(" §e/depthsickle statistic §7- §fKnow the number of times you have used the sickle.");
                commandSender.sendMessage("§6§m------------------------------------------------------------");
                commandSender.sendMessage("");
            }
        }

        return true;
    }

}
