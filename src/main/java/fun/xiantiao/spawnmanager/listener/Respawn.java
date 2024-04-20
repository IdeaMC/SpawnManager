package fun.xiantiao.spawnmanager.listener;

import fun.xiantiao.spawnmanager.GroupSpawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static fun.xiantiao.spawnmanager.SpawnManager.getGroupSpawns;
import static fun.xiantiao.spawnmanager.SpawnManager.getPlayerGroups;

/**
 * @author xiantiao
 * @date 2024/4/20
 * SpawnManager
 */
public final class Respawn implements Listener {
    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(getPlayerGroups(player).toString());

        // 迭代插件所有group
        for (GroupSpawn value : getGroupSpawns().values()) {
            player.sendMessage("value "+ value.getGroup());
            // 如果玩家在这个组里面
            if (getPlayerGroups(player).contains(value.getGroup())) {
                Location location = value.getSpawn().get(GroupSpawn.SpawnType.RESPAWN);
                if (location != null) {
                    event.setRespawnLocation(location);
                } else {
                    player.sendMessage("无法找到世界");
                }
                break;
            }
        }
    }
}
