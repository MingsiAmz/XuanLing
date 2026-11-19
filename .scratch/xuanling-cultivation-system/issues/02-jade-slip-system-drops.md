# 02: 玉简体系与掉落重构

**What to build:** JadeSlip 增加体系字段并持久化；普通修士默认体系为"仙修"，玉简显示"玉修·仙修"；仅天骄可能持有自定义体系名；玉简 tooltip 境界名走本地化，不再裸出技术 key；移除硬编码玉简掉落，改用 NeoForge 原生 loot table 按怪物境界分档（境界越高掉率越低，整体接近"50 只里小概率"）；顶级天骄特殊掉落（凋零星等）迁入对应 loot table。

**Blocked by:** None (can start immediately)

**Status:** resolved

- [x] JadeSlipData 增加 system 字段并更新 codec
- [x] JadeSlipItem.updateDisplayName 渲染"玉修·{system}"，默认"玉修·仙修"
- [x] JadeSlipItem.appendHoverText 境界名使用 Component.translatable 本地化
- [x] AutoGenerationEvents.attachNPCData 普通修士写入默认"仙修"体系，仅天骄写入自定义体系
- [x] 移除 AutoGenerationEvents.onLivingDeath 中的硬编码玉简掉落
- [x] 新增按境界分档的 NeoForge 原生 loot table，顶级天骄特殊掉落迁入其中
- [x] loot_modifiers 按实体 NBT/类型分发到对应 loot table

## Answer

已实现玉简体系与掉落重构：

1. `JadeSlipData` 新增 `system` 字段（默认 `DEFAULT_SYSTEM = "仙修"`），随 `CODEC` / `STREAM_CODEC` 持久化与网络同步，`withSystem` / `getSystem` 访问器。
2. `JadeSlipItem.updateDisplayName` 渲染 `玉修·{system}`（默认 `玉修·仙修`），空白玉简保留 `空白玉简`。
3. `JadeSlipItem.appendHoverText` 境界名改用 `Component.translatable`（对 `energy.*` 技术 key 走本地化），新增 `item.xuanling.jade_slip.tooltip.system` 体系 tooltip。
4. `AutoGenerationEvents.attachNPCData` 持久化 `system`（普通修士写 `仙修`，天骄写自定义体系名）、`top_prodigy` 标记、`template_id`；新增 `getNPCRealmLevel` / `isNPCTopProdigy` / `getNPCSystem` / `getNPCTemplateId` 供 loot 条件读取；移除 `LivingDeathEvent` 中硬编码玉简掉落及 `onLivingDeath`。
5. 新增 NeoForge 原生 loot table `xuanling:entities/jade_slip_tier0..tier4` 按境界分档（掉率 2%→0.15%，随境界递减），`jade_slip_prodigy` 吸收凋零星（1~3 枚）与顶级天骄玉简，掉率 25%。
6. `loot_modifiers/add_jade_slip_tier0..tier4` + `add_jade_slip_prodigy` 按 `xuanling:npc_realm_level` / `xuanling:npc_top_prodigy` NBT 条件分发；`global_loot_modifiers.json` 注册全部；移除旧 `xuanlingmod` 硬编码 loot 表与修饰器；`npc/prodigy.json` customLootTable 迁至 `xuanling:entities/jade_slip_prodigy`。

新增注册类：`RegisterLootConditions`（`npc_realm_level`、`npc_top_prodigy`）、`RegisterLootFunctions`（`create_jade_slip`，读取实体 NBT 生成带体系与境界的玉简）。Lang 补齐 `jade_slip.tooltip.system` 与自定义境界名（混沌/永恒/炼气/筑基/金丹/元婴）zh_cn/en_us。

