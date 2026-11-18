# 02: 境界修为平滑曲线与突破判定（服务端）

**What to build:** 服务端按 `needExp(level) = base × growthRate^level` 计算所需修为；修为积累遵循平滑曲线；突破 level 4+（元婴及以上）触发渡天劫状态，练气/筑基/金丹不触发天劫。

**Blocked by:** 01 (Config 新增项)

**Status:** resolved

- [x] `needExp(level)` 按平滑曲线计算，高境界所需修为指数级增长
- [x] 练气、筑基、金丹突破不进入渡天劫状态
- [x] 元婴（level 4）及以上突破时进入渡天劫状态

## Answer

Implemented in `src/main/java/com/mingsiamz/xuanlingmod/event/DataEvent.java` (`onPlayerTick`, `checkBreakthrough`, `handleTribulation`) and `src/main/java/com/mingsiamz/xuanlingmod/Config.java`:
- `Config.needExp(level) = REALM_EXP_BASE × REALM_EXP_GROWTH_RATE^level` (base 100, rate 1.6) — server-side exp curve.
- Realm exp accumulates during meditation (`speedGain = 0.1 × insight^0.6 × TPAO^0.4` per tick).
- `checkBreakthrough`: `currentLevel >= TRIBULATION_MIN_LEVEL (4)` → set `tribulating=true` with 400-tick countdown (渡天劫); below 4 → direct `realmLevel++` (练气/筑基/金丹不渡天劫).
- `handleTribulation`: lightning every 40 ticks, damage scaled by condemnation, on tick 0 → `realmLevel++` and `tribulating=false`.
Verified with `./gradlew compileJava` (no errors).
