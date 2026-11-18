# 05: 善恶标签计算与天劫伤害影响

**What to build:** 按 `morality / (morality + condemnation)` 占比计算善/中立/恶标签；善标签减免天劫伤害、恶标签增加天劫伤害，极高道德值时可无需渡劫。

**Blocked by:** 04 (道德值与天谴值按击杀行为增减)

**Status:** ready-for-human

## Comments
Implemented in commit 2fee6cf (depends on 04). 按 morality / (morality + condemnation) 占比计算善/中立/恶标签（分母为 0 中立）；≥ 0.8 善、≤ 0.2 恶，其余中立（阈值由 Config.alignment_good_threshold / alignment_evil_threshold 控制）。善标签天劫伤害 ×0.5，恶标签 ×1.5，中立 ×1.0；极高道德值（≥ 90% 上限）无需渡劫直接突破。

- [x] 道德占比 ≥ 0.8 时贴“善”标签
- [x] 道德占比 ≤ 0.2 时贴“恶”标签
- [x] 其余占比贴“中立”标签
- [x] 善标签下天劫伤害减免，恶标签下天劫伤害增加，极高善可无需渡劫
