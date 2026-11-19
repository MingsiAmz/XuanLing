# 03: 凡人杀怪修为与实时进度

**What to build:** 仅 level 0 凡人击杀生物可按目标生命值与护甲值平滑计算获得修为；level ≥1 杀怪不再增加修为；击杀后主动同步附件到客户端，使 statgui 修为条实时推进；修为足够时凡人可直接突破到练气（level<4 不渡天劫）。

**Blocked by:** 01 (UI 基础与本地化)

**Status:** resolved

## Answer

Implemented 凡人杀怪修为与实时进度 in `DataEvent.onEntityDeath`:
- 仅 killer `BasicData.getRealmLevel() == 0`（凡人）授予修为；level ≥1 杀怪不再增加修为。
- 修为增量按平滑曲线 `Config.getKillRealmExpBase() * Math.sqrt(victim.getMaxHealth() + victim.getArmorValue())` 计算，移除了原 `base + perLevel × victimRealm` 公式与废弃的 `getEntityRealmLevel` 方法。
- 击杀后调用 `checkBreakthrough(killerPlayer, killerData, level, serverPlayer)`，允许凡人直接突破到练气（`checkBreakthrough` 内部已处理 level<4 不渡天劫）。
- 击杀后重新附加 `BasicData`（`killerPlayer.setData(BASIC_DATA, BasicData.copy(killerData))`）触发客户端附件同步，使 `StateGui` 修为条按 `realmExp / needExp(level)` 百分比实时推进（StateGui 百分比渲染已由 01 完成，此处仅依赖同步）。
- 编译通过（`./gradlew compileJava` BUILD SUCCESSFUL）。仓库无游戏测试/模组专用 JUnit，类检查以编译为准。

- [ ] DataEvent.onEntityDeath 仅在 killer realmLevel == 0 时授予修为
- [ ] 修为增量按 victim maxHealth + armor 的平滑曲线计算
- 击杀后重新附加 BasicData 到玩家，触发客户端附件同步
- 击杀后调用 checkBreakthrough，允许凡人直接突破到练气
- [ ] StateGui 修为条在杀怪后可见推进（依赖 01 的百分比渲染）
