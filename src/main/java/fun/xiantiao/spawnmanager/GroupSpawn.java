package fun.xiantiao.spawnmanager;

import com.sun.istack.internal.NotNull;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiantiao
 * @date 2024/4/20
 * SpawnManager
 * 管理单个组的出生点
 */
public class GroupSpawn {
    final String group;
    final long priority;
    final Map<SpawnType,Location> spawn;

    /**
     * 创建
     * @param group 组名称
     * @param priority 优先级
     * 设置优先级是为了未来的兼容
     */
    public GroupSpawn(@NotNull String group, long priority) {
        this.group = group;
        this.priority = priority;
        spawn = new HashMap<>();
    }

    /**
     * 设置spawn
     * @param spawner spawn类型
     * @param respawn 位置
     */
    public void setRespawn(SpawnType spawner, Location respawn) {
        this.spawn.put(spawner,respawn);
    }
    enum SpawnType {
        RESPAWN
    }
}
