# 04: 道德值与天谴值按击杀行为增减

**What to build:** 基于击杀事件的生物类型增减道德值/天谴值：击杀敌对生物（怪物）或高天谴值玩家/生物 → 道德值上升；击杀友好生物（动物）、中立生物或高道德值玩家/生物 → 天谴值上升。

**Blocked by:** None (can start immediately)

**Status:** ready-for-human

## Comments
Implemented in commit 2fee6cf. 击杀敌对生物提升道德值；击杀友好/中立生物增加天谴值；击杀高天谴值玩家或生物提升道德值；击杀高道德值玩家或生物增加天谴值。数值通过 Config.kill_morality_change / kill_condemnation_change 控制，由 BasicData.setMorality/setCondemnation 按上限 clamp。

- [x] 击杀敌对生物提升道德值
- [x] 击杀友好/中立生物增加天谴值
- [x] 击杀高天谴值玩家或生物提升道德值
- [x] 击杀高道德值玩家或生物增加天谴值
