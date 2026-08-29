# tf-c2me-compat（暮色森林 × C2ME 兼容补丁）

## 这是什么

一个让 **暮色森林**（[Twilightforest-Re26](https://github.com/Lonmo0208/Twilightforest-Re26) 26.2 Fabric 版）和 **C2ME** 完全兼容的小型补丁 mod。

C2ME 是区块生成性能优化 mod。

## 装了有什么效果

- 暮色森林维度正常使用 C2ME 的多线程区块生成（不装本 mod 时 C2ME 也能多线程，但需要本 mod 修复的几处线程安全/崩溃问题）；
- 暮色森林的地形函数接入 dfc 编译器，地形计算更快（不再走低效的逐点回退）；
- 安装了 [C2ME OpenCL 加速模块](https://modrinth.com/mod/c2me-ocl) 的环境下不再崩溃：主世界/下界/末地走 GPU 加速，暮色森林维度自动回退 CPU 生成（暮色地形依赖群系查表，原理上无法进 GPU，这是唯一例外）。

## 怎么用

1. 准备好这些 mod（都放进 `mods` 文件夹）：
   - 暮色森林（Twilightforest-Re26 26.2 Fabric）
   - C2ME（[Modrinth](https://modrinth.com/mod/c2me) 26.2 Fabric 版）
   - Fabric API
   - **本 mod**（去 [Releases](../../releases) 下载，或自行构建）
2. 可选：想启用 OpenCL GPU 加速，再装 [C2ME OpenCL 模块](https://modrinth.com/mod/c2me-ocl)（匹配 C2ME 版本）和 ScalableLux；
3. 不需要任何配置。装完进游戏即可。

日志里出现 `[TF-C2ME-Compat] Twilight Forest <-> C2ME dfc bindings active` 就说明生效了。

## 已知限制

- 暮色森林的"群系决定地形"路由器（`TerrainDensityRouter` 等）依赖在 Java 侧查询群系注册表，原理上无法进 GPU 内核——所以暮色维度不走 OpenCL，其余一切照常；
- 崩溃时请附带 `logs/latest.log` 反馈。

## 自行构建（给开发者）

不使用 loom：本 mod 不调用任何 Minecraft 成员（只用类名做泛型边界），MC 26.2 已不混淆，无需重映射。

先把以下 jar 放入 `libs/`（**C2ME 为 ARR 协议，禁止再分发，请自行获取**）：

| jar | 来源 |
|---|---|
| `twilightforest-26.2.jar` | 自行构建 Twilightforest-Re26（26.2.x-Fabric 分支 `gradlew build`） |
| `c2me-fabric-opts-dfc.jar` | C2ME 主 jar 解包 `META-INF/jars/` |
| `c2me-fabric-base.jar` | 同上（`-all.jar`） |
| `c2me-fabric-opts-accel-opencl.jar` | C2ME OpenCL 模块（可选，仅 OpenCL 路径需要） |
| `minecraft-common-26.2.jar` | 任意 loom 26.2 项目缓存，或 MC 26.2 服务端 jar |
| `fabric-loader-0.19.3.jar` | [FabricMC maven](https://maven.fabricmc.net/net/fabricmc/fabric-loader/) |
| `asm-9.10.1.jar` / `asm-commons-9.10.1.jar`、`slf4j-api-2.0.16.jar` | Maven Central |

```bash
./gradlew build   # 产物在 build/libs/tfc2mecompat-*.jar
```

## 实测环境

MC 26.2 + Fabric Loader 0.19.5 + C2ME 0.4.2-alpha.0.43 + OpenCL 模块+ 暮色森林 4.8.4208：主世界/下界/末地 OpenCL 正常运行，长时间游玩无崩溃。

## 许可

MIT。C2ME 属其作者所有（ARR），本 mod 不包含、不修改、不分发 C2ME 的任何代码，仅通过其公开注册表接口（`McToAst` / `BytecodeGenRegistry` / `OpenCLCGenData`）注册兼容内容。
