# tf-c2me-compat

暮色森林 (Twilight Forest Re-26 Fabric) × C2ME 的密度函数编译兼容层。
**不修改 C2ME 任何代码**（C2ME 为 ARR 协议），仅通过 C2ME dfc 的公开注册接口
（`McToAst.REGISTRY` / `BytecodeGenRegistry.REGISTRY` / `OpenCLCGenData.REGISTRY`）
把暮色森林的自定义密度函数接入 dfc 编译器。

## 原理

C2ME dfc 遇到不认识的 DensityFunction 时会包成 `DelegateNode`（Java 回调）。
JVM 编译路径可以工作，但 OpenCL 内核生成器对 `DelegateNode` 直接抛
`UnsupportedOperationException`，导致整棵密度函数树的 GPU 编译失败。

本 mod 做三件事：

1. **新增 `ExpNode`**（dfc 节点集里缺 exp），自带 JVM 字节码 emitter 和
   OpenCL C emitter（`exp()`），注册进两个生成器注册表。
2. **把 7 个 TF 密度函数 1:1 翻译成 dfc AST**（`TanhHillFunction` 的
   `Math.exp`/`tanh` 项用 `ExpNode` 表达；`BoxDensityFunction` 用自定义
   `BeardBoxNode`；分支用 `RangeChoiceNode` 守卫，常量字段在编译期消解）。
   JVM 与 OpenCL 双路径同时受益。
3. **防御式注册**：任何失败都只记日志回退，不会让游戏崩溃。

## 已绑定

| TF 密度函数 | 方式 |
|---|---|
| `SqrtDensityFunction` | `SqrtNode` |
| `AbsoluteDifferenceFunction.Min/Max` | `AbsNode`+`Min/MaxNode` |
| `FocusedDensityFunction` | 球面距离 + clampedMap 展开 |
| `HollowHillFunction` | `CosNode`/`SqrtNode` + RangeChoice 守卫 |
| `TanhHillFunction` | `ExpNode`(tanh/exp 项) + RangeChoice 守卫 |
| `BoxDensityFunction` | 自定义 `BeardBoxNode`：JVM 路径逐位精确调用 vanilla `Beardifier` 静态方法；OpenCL 路径全内联（含复刻的 `BEARD_KERNEL` 24³ 核表，纯 `exp()` 公式生成 + Quake `fastInvSqrt` 位技巧） |

## 未绑定（保留 DelegateNode 回退）

- `NoiseDensityRouter` / `ChunkCachedNoiseDensityRouter` — 按列采样群系注册表
  （`BiomeDensitySource.sampleTerrain`），原理上无法进入无状态 GPU 内核。

## 构建

本 mod 刻意不使用 loom——它不调用任何 Minecraft 成员（仅泛型边界用到类名），
且 MC 26.2 已不混淆，因此无需重映射，普通 `java` 插件即可构建。

构建前把以下运行时 jar 放入 `libs/`（**不要提交到 git**，C2ME 为 ARR 协议禁止再分发）：

| jar | 来源 |
|---|---|
| `twilightforest-26.2.jar` | 自行构建 [Twilightforest-Re26](https://github.com/Lonmo0208/Twilightforest-Re26)（26.2.x-Fabric 分支 `gradlew build`） |
| `c2me-fabric-opts-dfc.jar` | [C2ME](https://modrinth.com/mod/c2me) 26.2 版主 jar 解包（`META-INF/jars/`） |
| `c2me-fabric-base.jar` | 同上（`-all.jar`） |
| `c2me-fabric-opts-accel-opencl.jar` | [C2ME OpenCL 加速模块](https://modrinth.com/mod/c2me-ocl)（可选，仅 OpenCL 路径需要） |
| `minecraft-common-26.2.jar` | 任意 loom 26.2 项目缓存，或 MC 26.2 服务端 jar |
| `fabric-loader-0.19.3.jar` | [FabricMC maven](https://maven.fabricmc.net/net/fabricmc/fabric-loader/) |
| `asm-9.10.1.jar` / `asm-commons-9.10.1.jar` | Maven Central |
| `slf4j-api-2.0.16.jar` | Maven Central |

然后：

```bash
./gradlew build   # 产物在 build/libs/tfc2mecompat-*.jar
```

## 使用

与暮色森林、C2ME（及可选的 OpenCL 模块）一起放入 `mods/` 即可，无需配置。
兼容 mod 会自动把 `openclAccel.allowIncompatibilityFallback` 强制为开启，
使无法 GPU 编译的世界（暮色森林）优雅回退到 CPU 生成而不是崩溃。

## 验证状态

已在 MC 26.2 + Fabric 0.19.5 + C2ME 0.4.2-alpha.0.43 + OpenCL 模块（Quadro P620）下实测：
主世界/下界/末地 OpenCL 正常编译运行；暮色森林维度按设计优雅回退（日志一条
`OpenCL codegen ... failed` 警告），多线程 + dfc JVM 编译全速工作，无崩溃。
