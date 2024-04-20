package fun.xiantiao.spawnmanager;

import com.sun.istack.internal.NotNull;
import fun.xiantiao.spawnmanager.listener.Join;
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
    static SpawnManager instance;

    //优先级与单个GroupSpawn管理器
    // 这里不使用List是因为需要TreeMap进行优先级排序
    private static Map<Long, GroupSpawn> groupSpawns;
    @Override
    public void onEnable() {
        getLogger().info("SpawnManager 加载中");

        saveDefaultConfig();
        reloadConfig();

        instance = this;

        groupSpawns =  new TreeMap<>(Collections.reverseOrder()); // groupSpawns为降序
        // 读取所有group的key
        for (String group : getConfig().getConfigurationSection("group").getKeys(false)) {
            // 获取组优先级
            long priority = getConfig().getLong("group." + group + ".priority");

            // 单个组的管理器
            GroupSpawn groupSpawn = new GroupSpawn(group,priority);

            // 组的重生点
            Location RESPAWN = serializeLocation(getConfig().getString("group." + group + ".Respawn"));
            groupSpawn.setRespawn(SpawnType.RESPAWN,RESPAWN);
            // 组的首次加入点
            Location FIRSTJOIN = serializeLocation(getConfig().getString("group." + group + ".FirstJoin"));
            groupSpawn.setRespawn(SpawnType.FIRSTJOIN,FIRSTJOIN);
            // 组的加入点
            Location JOIN = serializeLocation(getConfig().getString("group." + group + ".Join"));
            groupSpawn.setRespawn(SpawnType.JOIN,JOIN);


            // 添加
            groupSpawns.put(priority,groupSpawn);
        }

        // 注册事件
        getServer().getPluginManager().registerEvents(new Respawn(),this);
        getServer().getPluginManager().registerEvents(new Join(),this);

        // info
        getLogger().info("info");
        // 获取所有组
        for (GroupSpawn groupSpawn : groupSpawns.values()) {
            getLogger().info("  group: "+groupSpawn.getGroup());
            getLogger().info("    priority: "+groupSpawn.getPriority());
            getLogger().info("      RESPAWN:   "+ deserializationLocation(groupSpawn.getSpawn().get(SpawnType.RESPAWN)));
            getLogger().info("      FIRSTJOIN: "+ deserializationLocation(groupSpawn.getSpawn().get(SpawnType.FIRSTJOIN)));
            getLogger().info("      JOIN:      "+ deserializationLocation(groupSpawn.getSpawn().get(SpawnType.JOIN)));
        }
    }

    /**
     * 获取玩家在哪些组
     * @param player 玩家
     * @return 组名字
     */
    @NotNull public static List<String> getPlayerGroups(Player player) {
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

    /**
     * 获取插件的组都有哪些
     * @return 优先级与组spawn管理器
     */
    @NotNull public static Map<Long, GroupSpawn> getGroupSpawns() {
        return groupSpawns;
    }

    public static SpawnManager getInstance() {
        return instance;
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
        if (world == null) {
            return null;
        }
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        return world+" "+x+" "+y+" "+z+" "+yaw+" "+pitch;
    }
}
