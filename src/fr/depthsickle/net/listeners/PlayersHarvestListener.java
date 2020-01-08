package fr.depthsickle.net.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.songoda.skyblock.api.SkyBlockAPI;
import com.songoda.skyblock.api.island.IslandManager;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import fr.depthsickle.net.Main;
import fr.depthsickle.net.helpers.actions.ActionBar;
import fr.depthsickle.net.helpers.modes.Autosell;
import fr.depthsickle.net.helpers.modes.Harvest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

import java.util.concurrent.ThreadLocalRandom;

public class PlayersHarvestListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent playerInteractEvent) {
        if (Main.getInstance().getVersion().contains("1_12") || Main.getInstance().getVersion().contains("1_13") || Main.getInstance().getVersion().contains("1_14") || Main.getInstance().getVersion().contains("1_15")) {
            if (playerInteractEvent.getAction().equals(Action.PHYSICAL)) return;
            if (!playerInteractEvent.getHand().equals(EquipmentSlot.HAND)) return;
        }

        if (playerInteractEvent.getPlayer().getItemInHand().getType() == null || playerInteractEvent.getPlayer().getItemInHand().getType().equals(Material.AIR)) return;
        if (playerInteractEvent.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (!playerInteractEvent.getAction().equals(Action.valueOf(Main.getInstance().getAction().toString()))) return;
        if (!playerInteractEvent.getPlayer().getItemInHand().getType().equals(Material.matchMaterial(Main.getInstance().getConfig().getString("Material.Item")))) return;
        if (!playerInteractEvent.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("Material.Name").replace("&", "§"))) return;

        playerInteractEvent.setCancelled(true);

        if (!playerInteractEvent.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noGamemode").replace("&", "§"));
            }
            return;
        }

        if (!Main.getInstance().getWorld().contains(playerInteractEvent.getClickedBlock().getLocation().getWorld().getName())) {
            if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noWorld").replace("&", "§"));
            }
            return;
        }

        if (!Main.getInstance().getCrop().contains(playerInteractEvent.getClickedBlock().getType().toString())) {
            if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noCrop").replace("&", "§"));
            }
            return;
        }

        if (Main.getInstance().isWorldGuard()) {
            if (!playerInteractEvent.getPlayer().hasPermission("Permission.Bypass") && !WorldGuardPlugin.inst().canBuild(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock().getLocation())) {
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                    this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noRegion").replace("&", "§"));
                }
                return;
            }
        }

        if (Main.getInstance().isaSkyblock()) {
            if (!playerInteractEvent.getPlayer().hasPermission("Permission.Bypass") && !ASkyBlockAPI.getInstance().locationIsOnIsland(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock().getLocation()) && playerInteractEvent.getClickedBlock().getLocation().getWorld().equals(ASkyBlockAPI.getInstance().getIslandWorld())) {
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                    this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noRegion").replace("&", "§"));
                }
                return;
            }
        }

        if (Main.getInstance().isuSkyblock()) {
            uSkyBlockAPI uSkyBlockAPI = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");

            if (uSkyBlockAPI != null && uSkyBlockAPI.isEnabled()) {
                if (!playerInteractEvent.getPlayer().hasPermission("Permission.Bypass") && !uSkyBlockAPI.getIslandInfo(playerInteractEvent.getClickedBlock().getLocation()).getOnlineMembers().contains(playerInteractEvent.getPlayer())) {
                    if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                        this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noRegion").replace("&", "§"));
                    }
                    return;
                }
            }
        }

        if (Main.getInstance().isFabledSkyblock()) {
            final IslandManager islandManager = SkyBlockAPI.getIslandManager();

            if (!islandManager.hasPermission(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock().getLocation(), "Destroy")) {
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                    this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noRegion").replace("&", "§"));
                }
                return;
            }
        }

        if (Main.getInstance().getFactions().equals("SavageFactions")) {
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(playerInteractEvent.getPlayer());
            FLocation fLocation = new FLocation(playerInteractEvent.getClickedBlock().getLocation());

            if (!playerInteractEvent.getPlayer().hasPermission("Permission.Bypass") && !Board.getInstance().getFactionAt(fLocation).isWilderness() && !fPlayer.isInOwnTerritory()) {
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                    this.send(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getLangConfiguration().getString("noRegion").replace("&", "§"));
                }
                return;
            }
        }

        if (Main.getInstance().getVersion().contains("1_13") || Main.getInstance().getVersion().contains("1_14") || Main.getInstance().getVersion().contains("1_15")) {
            playerInteractEvent.getPlayer().sendMessage("§d<-> Block : "+ playerInteractEvent.getClickedBlock() +" | Material : "+ playerInteractEvent.getClickedBlock().getType());
            return;
        }

        switch (playerInteractEvent.getClickedBlock().getType()) {
            case CARROT:
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("harvest")) {
                    new Harvest().harvestItem(
                            playerInteractEvent.getPlayer(),
                            ThreadLocalRandom.current().nextInt(1 , 6),
                            playerInteractEvent.getClickedBlock(),
                            Material.CARROT_ITEM,
                            Material.CARROT,
                            7
                    );
                }

                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("autosell")) {
                    new Autosell().economySeed(
                            playerInteractEvent.getPlayer(),
                            Main.getInstance().getConfig().getDouble("Hook.Economy.Price.CARROT"),
                            ThreadLocalRandom.current().nextInt(1 , 6),
                            playerInteractEvent.getClickedBlock(),
                            Material.CARROT_ITEM,
                            Material.CARROT,
                            7
                    );
                }
                break;
            case POTATO:
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("harvest")) {
                    new Harvest().harvestItem(
                            playerInteractEvent.getPlayer(),
                            ThreadLocalRandom.current().nextInt(1 , 6),
                            playerInteractEvent.getClickedBlock(),
                            Material.POTATO_ITEM,
                            Material.POTATO,
                            7
                    );
                }

                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("autosell")) {
                    new Autosell().economySeed(
                            playerInteractEvent.getPlayer(),
                            Main.getInstance().getConfig().getDouble("Hook.Economy.Price.POTATO"),
                            ThreadLocalRandom.current().nextInt(1, 6),
                            playerInteractEvent.getClickedBlock(),
                            Material.POTATO_ITEM,
                            Material.POTATO,
                            7
                    );
                }
                break;
            case BEETROOT_BLOCK:
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("harvest")) {
                    new Harvest().harvestItem(
                            playerInteractEvent.getPlayer(),
                            1,
                            playerInteractEvent.getClickedBlock(),
                            Material.BEETROOT,
                            Material.BEETROOT_BLOCK,
                            3
                    );
                }

                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("autosell")) {
                    new Autosell().economySeed(
                            playerInteractEvent.getPlayer(),
                            Main.getInstance().getConfig().getDouble("Hook.Economy.Price.BEETROOT_BLOCK"),
                            1,
                            playerInteractEvent.getClickedBlock(),
                            Material.BEETROOT,
                            Material.BEETROOT_BLOCK,
                            3
                    );
                }
                break;
            case CROPS:
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("harvest")) {
                    new Harvest().harvestSeed(
                            playerInteractEvent.getPlayer(),
                            1,
                            playerInteractEvent.getClickedBlock(),
                            Material.WHEAT,
                            Material.SEEDS,
                            7,
                            59
                    );
                }

                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("autosell")) {
                    new Autosell().economyID(
                            playerInteractEvent.getPlayer(),
                            Main.getInstance().getConfig().getDouble("Hook.Economy.Price.CROPS"),
                            1,
                            playerInteractEvent.getClickedBlock(),
                            Material.WHEAT,
                            59,
                            7
                    );
                }
                break;
            case NETHER_WARTS:
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("harvest")) {
                    new Harvest().harvestItem(
                            playerInteractEvent.getPlayer(),
                            ThreadLocalRandom.current().nextInt(1 , 5),
                            playerInteractEvent.getClickedBlock(),
                            Material.NETHER_STALK,
                            Material.NETHER_WARTS,
                            3
                    );
                }

                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("autosell")) {
                    new Autosell().economySeed(
                            playerInteractEvent.getPlayer(),
                            Main.getInstance().getConfig().getDouble("Hook.Economy.Price.NETHER_WARTS"),
                            ThreadLocalRandom.current().nextInt(2, 5),
                            playerInteractEvent.getClickedBlock(),
                            Material.NETHER_STALK,
                            Material.NETHER_WARTS,
                            3
                    );
                }
                break;
            case MELON_BLOCK:
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("harvest")) {
                    new Harvest().harvestBlock(
                            playerInteractEvent.getPlayer(),
                            ThreadLocalRandom.current().nextInt(3, 7),
                            playerInteractEvent.getClickedBlock(),
                            Material.MELON
                    );
                }

                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("autosell")) {
                    new Autosell().economyBlock(
                            playerInteractEvent.getPlayer(),
                            Main.getInstance().getConfig().getDouble("Hook.Economy.Price.MELON"),
                            ThreadLocalRandom.current().nextInt(3, 7),
                            playerInteractEvent.getClickedBlock(),
                            Material.MELON_BLOCK
                    );
                }
                break;
            case PUMPKIN:
                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("harvest")) {
                    new Harvest().harvestBlock(
                            playerInteractEvent.getPlayer(),
                            1,
                            playerInteractEvent.getClickedBlock(),
                            Material.PUMPKIN
                    );
                }

                if (Main.getInstance().getAccountManager().getAccount().get(playerInteractEvent.getPlayer().getName()).getMode().equals("autosell")) {
                    new Autosell().economyBlock(
                            playerInteractEvent.getPlayer(),
                            Main.getInstance().getConfig().getDouble("Hook.Economy.Price.PUMPKIN"),
                            1,
                            playerInteractEvent.getClickedBlock(),
                            Material.PUMPKIN
                    );
                }
                break;
        }
    }

    private void send(Player player, String type, String message) {
        switch (type) {
            case "COMMON":
                player.sendMessage(message);
                break;
            case "ACTIONBAR":
                new ActionBar().send(player, message);
                break;
        }
    }

}
