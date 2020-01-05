package fr.depthsickle.net.listeners;

import fr.depthsickle.net.Main;
import fr.depthsickle.net.helpers.librairy.material.Items;
import fr.depthsickle.net.objects.Players;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class PlayersManagerListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
        if (!Main.getInstance().getPlayersManager().getAccount().containsKey(playerJoinEvent.getPlayer().getName())) {
            Main.getInstance().getPlayersManager().getAccount().put(playerJoinEvent.getPlayer().getName(), new Players(playerJoinEvent.getPlayer().getName(), true, 0, "harvest"));
            return;
        }

        Main.getInstance().checkUpdate(playerJoinEvent.getPlayer());
        Main.getInstance().getPlayersManager().added(playerJoinEvent.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent playerQuitEvent) {
        Main.getInstance().getPlayersManager().removed(playerQuitEvent.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getPlayer().getItemInHand().getType() == null || playerInteractEvent.getPlayer().getItemInHand().getType().equals(Material.AIR)) return;
        if (playerInteractEvent.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (!playerInteractEvent.getAction().equals(Action.valueOf(Main.getInstance().getAction().toString()))) return;
        if (!playerInteractEvent.getPlayer().getItemInHand().getType().equals(Material.matchMaterial(Main.getInstance().getConfig().getString("Material.Item")))) return;
        if (!playerInteractEvent.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("Material.Name").replace("&", "§"))) return;

        if (playerInteractEvent.getPlayer().isSneaking() && playerInteractEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK) || playerInteractEvent.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER,"Sickle");

            inventory.setItem(1, new Items(Material.GOLD_HOE).name("§6Harvest").lore("§7Pick up and planted your plantings automatically.").build());
            inventory.setItem(3, new Items(Material.CHEST).name("§6Auto-Sell").lore("§7Sell your plantings automatically. and replanted.").build());

            playerInteractEvent.getPlayer().openInventory(inventory);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent inventoryClickEvent) {

    }

}
