package fr.depthsickle.net.listeners;

import com.massivecraft.factions.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import fr.depthsickle.net.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

public class PlayersHarvestListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent playerInteractEvent) {
        if (Main.getInstance().getVersion().contains("1_12")) {
            if (playerInteractEvent.getAction().equals(Action.PHYSICAL)) return;
            if (!playerInteractEvent.getHand().equals(EquipmentSlot.HAND)) return;
        }

        if (playerInteractEvent.getPlayer().getItemInHand().getType() == null || playerInteractEvent.getPlayer().getItemInHand().getType().equals(Material.AIR)) return;
        if (playerInteractEvent.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (!playerInteractEvent.getAction().equals(Action.valueOf(Main.getInstance().getAction().toString()))) return;
        if (!playerInteractEvent.getPlayer().getItemInHand().getType().equals(Material.matchMaterial(Main.getInstance().getConfig().getString("Material.Item")))) return;
        if (!playerInteractEvent.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("Material.Name").replace("&", "§"))) return;

        playerInteractEvent.setCancelled(true);

        if (!playerInteractEvent.getPlayer().hasPermission(Main.getInstance().getConfig().getString("Permission.Use"))) {
            if (Main.getInstance().getPlayersManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noAccessPermission").replace("&", "§"));
            }
            return;
        }

        if (!playerInteractEvent.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if (Main.getInstance().getPlayersManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noGamemode").replace("&", "§"));
            }
            return;
        }

        if (!Main.getInstance().getWorld().contains(playerInteractEvent.getClickedBlock().getLocation().getWorld().getName())) {
            if (Main.getInstance().getPlayersManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noWorld").replace("&", "§"));
            }
            return;
        }

        if (Main.getInstance().isWorldGuard()) {
            if (!WorldGuardPlugin.inst().canBuild(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock().getLocation())) {
                if (Main.getInstance().getPlayersManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                    Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noRegion").replace("&", "§"));
                }
                return;
            }
        }

        if (Main.getInstance().isASkyblock()) {
            if (!ASkyBlockAPI.getInstance().locationIsOnIsland(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock().getLocation()) && playerInteractEvent.getClickedBlock().getLocation().getWorld().equals(ASkyBlockAPI.getInstance().getIslandWorld())) {
                if (Main.getInstance().getPlayersManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                    Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noRegion").replace("&", "§"));
                }
                return;
            }
        }

        if (Main.getInstance().isUSkyblock()) {
            uSkyBlockAPI uSkyBlockAPI = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");

            if (uSkyBlockAPI != null && uSkyBlockAPI.isEnabled()) {
                if (!uSkyBlockAPI.getIslandInfo(playerInteractEvent.getClickedBlock().getLocation()).getOnlineMembers().contains(playerInteractEvent.getPlayer())) {
                    Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noRegion").replace("&", "§"));
                    return;
                }
            }
        }

        if (Main.getInstance().getFactions().equals("SavageFactions")) {
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(playerInteractEvent.getPlayer());
            FLocation fLocation = new FLocation(playerInteractEvent.getClickedBlock().getLocation());

            if (!Board.getInstance().getFactionAt(fLocation).isWilderness() && !fPlayer.isInOwnTerritory()) {
                if (Main.getInstance().getPlayersManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                    Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("noRegion").replace("&", "§"));
                }
                return;
            }
        }

        if (!Main.getInstance().getPlant().contains(playerInteractEvent.getClickedBlock().getType().toString())) {
            if (Main.getInstance().getPlayersManager().getAccount().get(playerInteractEvent.getPlayer().getName()).isToggle()) {
                Main.getInstance().getPluginsHelper().message(playerInteractEvent.getPlayer(), Main.getInstance().getConfig().getString("Configuration.Message"), Main.getInstance().getFileMessageConfiguration().getString("errorPlant").replace("&", "§"));
            }
            return;
        }

        switch (playerInteractEvent.getClickedBlock().getType()) {
            case CARROT:
                Main.getInstance().getPluginsHelper().harvestWithSeed(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock(), Material.CARROT, Material.CARROT_ITEM, 7);
                break;
            case POTATO:
                Main.getInstance().getPluginsHelper().harvestWithSeed(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock(), Material.POTATO, Material.POTATO_ITEM, 7);
                break;
            case BEETROOT_BLOCK:
                Main.getInstance().getPluginsHelper().harvestWithSeed(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock(), Material.BEETROOT_BLOCK, Material.BEETROOT, 3);
                break;
            case CROPS:
                Main.getInstance().getPluginsHelper().harvestWithID(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock(), Material.WHEAT, Material.CROPS, 59, 7);
                break;
            case NETHER_WARTS:
                Main.getInstance().getPluginsHelper().harvestWithSeed(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock(), Material.NETHER_WARTS, Material.NETHER_STALK, 3);
                break;
            case MELON_BLOCK:
                Main.getInstance().getPluginsHelper().harvestWithBlock(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock(), Material.MELON);
                break;
            case PUMPKIN:
                Main.getInstance().getPluginsHelper().harvestWithBlock(playerInteractEvent.getPlayer(), playerInteractEvent.getClickedBlock(), Material.PUMPKIN);
                break;
        }

    }

}
