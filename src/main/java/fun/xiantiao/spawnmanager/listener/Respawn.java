package fun.xiantiao.spawnmanager.listener;

import fun.xiantiao.spawnmanager.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

/**
 * @author xiantiao
 * @date 2024/4/20
 * SpawnManager
 */
public final class Respawn implements Listener {
    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        List<String> effectivePermissions = SpawnManager.getPlayerGroups(player);

    }
}
