# SpawnManager
玩家spawn管理器

```yaml
# 禁止重复的优先级
# 设置优先级最好每次+10 -10
#
# 位置参数为(World world, double x, double y, double z, float yaw, float pitch)
# 需要全部填写

group:
  # 默认组
  default:
    # 优先级
    priority: 50
    # 重生时
    Respawn: "world 0.00 0.00 0.00 0.00 0.00"
    # 第一次加入
    FirstJoin: "world 0.00 0.00 0.00 0.00 0.00"
    # 加入时
    Join: "world 0.00 0.00 0.00 0.00 0.00"

  vip:
    priority: 100
    Respawn: "world 0.00 0.00 0.00 0.00 0.00"
    FirstJoin: "world -213.52 67.00 80.55 0 0"
    Join: "world 0.00 0.00 0.00 0.00 0.00"
```