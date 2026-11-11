# 玄灵模组

基于 Minecraft 1.21.1 NeoForge 的修仙模组，提供灵力体系、功法组件可视化编程与自定义 GUI 系统。

## 功能特性

- **修仙体系**：灵力/内力条 HUD、境界与宗门属性、灵力存储
- **组件可视化编程**：基于画布与节点的拖拽式编辑器，支持功法/逻辑/渲染/数据分类组件
- **自定义 GUI 框架**：修仙主题 UI 组件库，支持流式/网格/浮动等布局，含面板、状态条、按钮与打字机特效标签
- **数据驱动**：本地 JSON 配置热重载与持久化
- **网络同步**：客户端与服务端修仙数据包同步

## 技术栈

- **框架**: NeoForge 1.21.1
- **语言**: Java 21
- **构建**: Gradle
- **序列化**: Gson, Minecraft Codec/StreamCodec

## 快速开始

1. 确保 JDK 21 及 Gradle 已安装
2. 克隆仓库并构建：
   ```bash
   git clone <repo-url>
   cd xuanling-template-1.21.1
   ./gradlew build
   ```
3. 启动客户端：
   ```bash
   ./gradlew runClient
   ```

## 配置说明

- **语言文件**: `src/generated/resources/assets/xuanling/lang/zh_cn.json`/ `en_us.json`
- **运行时配置**: 运行后生成于 `./config/xuanling/` 目录，由 `DataManager` 与 `DataLoader` 管理读写与热重载

## 开发：热重载（Remold）

dev 运行已接入 [Remold](https://github.com/I-hope1/Remold) **0.2.6**：`./gradlew runClient` 开着游戏改代码，
保存并编译后新逻辑直接生效，不用重启。

- 改方法体：开箱即用（任意 JVM）
- 加字段/方法（结构变更）：需要 DCEVM 版 JVM（JBR）；`gradle.properties` 里的 `remold.jbr` 已指向本机 JBR 21.0.9，换机器记得改
- 资源（`src/main/resources`）：Remold 不会自动重载游戏资源包，改完要 `gradlew processResources --offline`（或 `-t` 持续同步），再在游戏里按 `F3+T`（客户端）/ `/reload`（数据包）
- 看当前配置：`./gradlew remoldInfo`；临时关闭：`-Premold.enabled=false`

细节、实测结果与踩坑见 [`docs/remold-hot-reload.md`](docs/remold-hot-reload.md)。