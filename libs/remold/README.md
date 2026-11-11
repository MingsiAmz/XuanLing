# libs/remold —— Remold 热重载产物（vendored）

来源：[I-hope1/Remold](https://github.com/I-hope1/Remold) **0.2.6**，Apache-2.0。
本目录放了两个 jar，让 dev run 不需要联网拉 JitPack：

| 文件 | 来源 | 大小 |
| --- | --- | --- |
| `remold-agent.jar` | Release v0.2.6 的 `remold-agent.jar` | 1.48 MB |
| `remold-plugin-forge.jar` | Release v0.2.6 的 `remold-plugin-forge.jar` | 27 KB |
| `backup-0.2.5/*.jar` | 旧版 0.2.5，出问题可覆盖回去 | 1.44 MB / 19 KB |

官方 Release 下载：

```bash
curl -L -o libs/remold/remold-agent.jar        https://github.com/I-hope1/Remold/releases/download/v0.2.6/remold-agent-0.2.6.jar
curl -L -o libs/remold/remold-plugin-forge.jar https://github.com/I-hope1/Remold/releases/download/v0.2.6/remold-plugin-forge-0.2.6.jar
```

从源码重建（早期 0.2.5 用的办法，仍可用）：

```bash
git clone --depth 1 --branch v0.2.6 https://github.com/I-hope1/Remold.git /tmp/remold-src
cd /tmp/remold-src
./gradlew :core:shadowJar :plugin-forge:jar
cp core/build/libs/*all*.jar   <本项目>/libs/remold/remold-agent.jar
cp plugin-forge/build/libs/*.jar <本项目>/libs/remold/remold-plugin-forge.jar
```

注意：**agent 与 plugin-forge 必须同版本**一起换（上游明确说 plugin 和 core 要一起发）。

注：上游构建需要能访问 Gradle 发行包；本机当初是先用已缓存的 Gradle 9.2.1 跑通的（`gradlew` wrapper 指定的
9.7.1 在当时下载失败）。接线脚本与用法见 [`docs/remold-hot-reload.md`](../../docs/remold-hot-reload.md)。

许可证：Apache License 2.0，与上游一致；分发时请保留上游 LICENSE/NOTICE。
