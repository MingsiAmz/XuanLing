# 03: 寿命固定周期扣除（每 7 游戏天减 1 月）

**What to build:** 服务端按固定周期（每 7 个游戏天）扣除 1 月寿命，寿命下限为 0，不随境界浮动；扣除同步到客户端。

**Blocked by:** None (can start immediately)

**Status:** resolved

- [x] 每 7 个游戏天扣除 1 月寿命
- [x] 寿命不低于 0
- [x] 寿命变动同步到客户端

## Answer

Implemented in `src/main/java/com/mingsiamz/xuanlingmod/event/DataEvent.java` (`onPlayerTick`), `src/main/java/com/mingsiamz/xuanlingmod/data/BasicData.java`, and `src/main/java/com/mingsiamz/xuanlingmod/Config.java`:
- `Config.LIFESPAN_DAYS_PER_MONTH = 7`; each tick `onPlayerTick` computes `currentDay = level.getDayTime() / 24000`, and when `currentDay - lastDay >= 7` deducts `months = (currentDay - lastDay) / 7` months and advances `lastLifespanCheckDay`.
- `BasicData.setLifespanMonth` clamps at 0 (寿命不低于 0).
- `lifespanMonth`/`lastLifespanCheckDay` are carried by the `BASIC_DATA` attachment which uses `.sync(BasicData.STREAM_CODEC)`, so changes sync to the client automatically.
Verified with `./gradlew compileJava` (no errors).
