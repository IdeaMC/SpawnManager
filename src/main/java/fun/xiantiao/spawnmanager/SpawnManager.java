package fun.xiantiao.spawnmanager;

import com.sun.istack.internal.NotNull;
import fun.xiantiao.spawnmanager.listener.Respawn;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static fun.xiantiao.spawnmanager.GroupSpawn.SpawnType;

/**
 * @author 40482
 */
public final class SpawnManager extends JavaPlugin implements Listener {

    //优先级与单个GroupSpawn管理器
    // 这里不使用List是因为需要TreeMap进行优先级排序
    private Map<Long, GroupSpawn> groupSpawns;
    @Override
    public void onEnable() {
        getLogger().info("SpawnManager 加载中");

        saveDefaultConfig();
        reloadConfig();

        groupSpawns =  new TreeMap<>(Collections.reverseOrder()); // groupSpawns为降序
        // 读取所有group的key
        for (String group : getConfig().getConfigurationSection("group").getKeys(false)) {
            // 获取组优先级
            long priority = getConfig().getLong("group." + group + ".priority");

            // 单个组的管理器
            GroupSpawn groupSpawn = new GroupSpawn(group,priority);

            // 组的重生点
            Location respawn = serializeLocation(getConfig().getString("group." + group + ".Respawn"));
            groupSpawn.setRespawn(SpawnType.RESPAWN,respawn);

            groupSpawns.put(priority,groupSpawn);
        }


        getServer().getPluginManager().registerEvents(new Respawn(),this);

        getLogger().info("顺序");
        for (GroupSpawn groupSpawn : groupSpawns.values()) {
            Location location = groupSpawn.spawn.get(SpawnType.RESPAWN);
            if (location != null) {
                getLogger().info("  group: "+groupSpawn.group);
                getLogger().info("    priority: "+groupSpawn.priority);
                getLogger().info("      RESPAWN: "+ deserializationLocation(location));
            }
        }
    }

    public static List<String> getPlayerGroups(Player player) {
        List<String> groupPermissions = new ArrayList<>();
        // 获取玩家的所有权限附件
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            String permission = attachmentInfo.getPermission();
            // 检查权限是否以 "group." 开头
            if (permission.startsWith("group.")) {
                groupPermissions.add(permission.replaceAll("group.",""));
            }
        }
        return groupPermissions;
    }

    public Map<Long, GroupSpawn> getGroupSpawns() {
        return groupSpawns;
    }

    private Location serializeLocation(String locationString) {
        String[] s = locationString.split(" ");
        if (s.length == 6) {
            World world = getServer().getWorld(s[0]);
            double x = Double.parseDouble(s[1]);
            double y = Double.parseDouble(s[2]);
            double z = Double.parseDouble(s[3]);
            float yaw = Float.parseFloat(s[4]);
            float pitch = Float.parseFloat(s[5]);
            return new Location(world,x,y,z,yaw,pitch);
        } else {
            return null;
        }
    }
    private String deserializationLocation(@NotNull Location location) {
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        return world+" "+x+" "+y+" "+z+" "+yaw+" "+pitch;
    }
}