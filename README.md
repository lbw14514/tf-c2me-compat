# tf-c2me-compat（暮色森林 × C2ME 兼容补丁）

> **适用于**：MC **26.1.x / 26.2 / 26w14a**，Fabric 与 NeoForge 双加载器，对应暮色森林 [Twilightforest-Re26](https://github.com/Lonmo0208/Twilightforest-Re26) 的全部生产分支（26.1.x / 26.1.x-Fabric / 26.2.x / 26.2.x-Fabric / 26w14a / 26w14a-Fabric），C2ME 对应版本。**不支持** 26.3 开发线（`26.3.x-Fabric-dev`，尚在开发中、未实测 26.3c2me本就不稳定 api等等变化就失效 所以没做）。详见下方版本矩阵。

## 这是什么

一个让 **暮色森林**（[Twilightforest-Re26](https://github.com/Lonmo0208/Twilightforest-Re26) 各生产分支）和 **C2ME** 完全兼容的补丁 mod。

C2ME 是区块生成性能优化 mod。

## 装了有什么效果

- 暮色森林维度正常使用 C2ME 的多线程区块生成 ;
- 暮色森林的地形函数接入 dfc 编译器，地形计算更快；
- 装了 OpenCL 模块的环境下，开启 OpenCL 不再崩溃。

## 版本矩阵

| 下载物 | 加载器 | MC 版本 | 对应暮色森林分支 |
|---|---|---|---|
| `tfc2mecompat-*+mc26.x-fabric.jar` | Fabric | 26.1.x / 26.2 / 26w14a | `26.1.x-Fabric` / `26.2.x-Fabric` / `26w14a-Fabric` |
| `tfc2mecompat-*+mc26.x-neoforge.jar` | NeoForge | 26.1.x / 26.2 | `26.1.x` / `26.2.x` |

各分支间暮色森林的地形函数类形状完全一致，因此每个加载器只需一个 jar，配套对应加载器与 MC 版本的 C2ME 即可。

## 怎么用

1. 按版本矩阵准备好这些 mod（都放进 `mods` 文件夹）：
   - 暮色森林（Twilightforest-Re26 对应生产分支）
   - C2ME（[Modrinth](https://modrinth.com/mod/c2me)，对应加载器与 MC 版本）
   - Fabric API（仅 Fabric 端需要）
   - **本 mod**（去 [Releases](../../releases) 下载，或自行构建）
2. 可选：想启用 OpenCL GPU 加速，再装 [C2ME OpenCL 模块](https://modrinth.com/mod/c2me-ocl)（匹配 C2ME 版本）和 ScalableLux；
3. 不需要任何配置。装完进游戏即可。

日志里出现 `[TF-C2ME-Compat] Twilight Forest <-> C2ME dfc bindings active` 就说明生效了。


## 自行构建（给开发者）

不使用 loom：本 mod 不调用任何 Minecraft 成员（只用类名做泛型边界），MC 26.x 已不混淆，无需重映射。

先把以下 jar 放入 `libs/`（**C2ME 为 ARR 协议，禁止再分发，请自行获取**）：

| jar | 来源 |
|---|---|
| `twilightforest-26.2.jar` | 自行构建 Twilightforest-Re26（26.2.x-Fabric 分支 `gradlew build`；其余分支绑定类一致，可复用） |
| `c2me-fabric-opts-dfc.jar` | C2ME 主 jar 解包 `META-INF/jars/` |
| `c2me-fabric-base.jar` | 同上（`-all.jar`） |
| `c2me-fabric-opts-accel-opencl.jar` | C2ME OpenCL 模块（可选，仅 OpenCL 路径需要） |
| `minecraft-common-26.2.jar` | 任意 loom 26.2 项目缓存，或 MC 26.2 服务端 jar |
| `fabric-loader-0.19.3.jar` | [FabricMC maven](https://maven.fabricmc.net/net/fabricmc/fabric-loader/) |
| `fml-loader-11.0.16.jar` | [NeoForge maven](https://maven.neoforged.net/)（仅 NeoForge jar 需要） |
| `asm-9.10.1.jar` / `asm-commons-9.10.1.jar`、`slf4j-api-2.0.16.jar` | Maven Central |

```bash
./gradlew build   # 产出 build/libs/ 下 fabric / neoforge 两个 jar
```

## 实测环境

MC 26.2 + Fabric Loader 0.19.5 + C2ME 0.4.2-alpha.0.43 + OpenCL 模块+ 暮色森林 4.8.4208：主世界/下界/末地 OpenCL 正常运行，长时间游玩无崩溃。

## 许可

MIT。C2ME 属其作者所有（ARR），本 mod 不包含、不修改、不分发 C2ME 的任何代码，仅通过其公开注册表接口（`McToAst` / `BytecodeGenRegistry` / `OpenCLCGenData`）注册兼容内容。
