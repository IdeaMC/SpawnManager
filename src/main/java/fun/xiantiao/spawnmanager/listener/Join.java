package fun.xiantiao.spawnmanager.listener;

import fun.xiantiao.spawnmanager.GroupSpawn;
import fun.xiantiao.spawnmanager.SpawnManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static fun.xiantiao.spawnmanager.SpawnManager.getGroupSpawns;
import static fun.xiantiao.spawnmanager.SpawnManager.getPlayerGroups;

/**
 * @author xiantiao
 * @date 2024/4/20
 * SpawnManager
 */
public class Join implements Listener {

    static Set<UUID> uuids = new HashSet<>(); // 没有玩过的玩家

    @EventHandler
    public void a(AsyncPlayerPreLoginEvent event) {
        if (!SpawnManager.getInstance().getServer().getOfflinePlayer(event.getUniqueId()).hasPlayedBefore()) {
            uuids.add(event.getUniqueId());
        }
    }
    @EventHandler
    public void a(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        // 迭代插件所有group
        for (GroupSpawn value : getGroupSpawns().values()) {
            // 如果玩家在这个组里面
            if (getPlayerGroups(player).contains(value.getGroup())) {
                Location location;
                if (uuids.contains(uniqueId)) {
                    uuids.remove(uniqueId);
                    location = value.getSpawn().get(GroupSpawn.SpawnType.FIRSTJOIN);
                } else {
                    location = value.getSpawn().get(GroupSpawn.SpawnType.JOIN);
                }

                if (location != null) {
                    player.teleport(location);
                } else {
                    player.sendMessage("无法找到世界");
                }
                break;
            }
        }



    }
}
