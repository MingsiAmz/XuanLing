# 01: UI 基础与本地化

**What to build:** 详细数据面板不再显示境界标签与修为条；statgui 修为条实时按 realmExp / needExp 推进，百分比作为居中中间文字且不超出条体，不显示 exp 数字；UI 标签（道德值、天谴值、寿命、善恶标签、修为）走 xuanlinglangpro 本地化，不再裸出 xuanling.energy.xxx 等技术 key。

**Blocked by:** None (can start immediately)

**Status:** ready-for-agent

- [x] DetailDataGui 移除境界标签、修为进度条与相关字段
- [x] DetailDataGui 剩余字面量标签改为 Label(key, value) 走本地化
- [x] StateGui 修为条每帧按当前境界与修为计算百分比，showText(false)
- [x] StateGui 在修为条中间渲染百分比文字，宽度不超出条体
- [x] XuanlingLangPro zh_cn/en_us 补齐 morality、condemnation、lifespan、alignment、realm_exp 键值
