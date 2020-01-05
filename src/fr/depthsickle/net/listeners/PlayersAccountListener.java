package fr.depthsickle.net.listeners;

import fr.depthsickle.net.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayersAccountListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
        if (!Main.getInstance().getAccountManager().getAccount().containsKey(playerJoinEvent.getPlayer().getName())) {
            Main.getInstance().getAccountManager().create(playerJoinEvent.getPlayer().getName());
            return;
        }

        Main.getInstance().getAccountManager().add(playerJoinEvent.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent playerQuitEvent) {
        Main.getInstance().getAccountManager().remove(playerQuitEvent.getPlayer().getName());
    }

}
