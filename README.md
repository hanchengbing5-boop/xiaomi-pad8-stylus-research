# 小米平板 8 触控笔适配研究

本仓库记录小米平板 8（`yupei`）与旧款小米灵感触控笔、戴尔 PN7522W 的离线分析，以及针对 [MaxMiPad](https://github.com/YifePlayte/MaxMiPad) 的实验补丁。**没有完成实笔测试；这里没有可用的适配 APK，也不保证第三方或旧款触控笔能在平板 8 上书写。**

## 已确认的边界

- 小米官方列出的平板 8 兼容笔是 Focus Pen 与 Focus Pen Pro。[官方兼容清单](https://www.mi.com/global/support/faq/details/KA-544109/)
- [平板 6 的 Novatek 驱动](https://github.com/MiCode/Xiaomi_Kernel_OpenSource/blob/pipa-t-oss/drivers/input/touchscreen/nt36532/nt36xxx.c)会把旧笔连接值 `2` 作为连接处理；[平板 8 的驱动](https://github.com/MiCode/vendor_xiaomi_proprietary_touch-driver/blob/yupei-w-oss/p81/nt36532/nt36xxx.c)把值 `1/2` 作为屏蔽分支，仅对 `3`（M80P）和 `8`（P81C）执行对应连接逻辑。
- 戴尔 [PN7522W 规格](https://www.dell.com/support/manuals/en-ph/dell-ap-pn7522w/dell-ap-pn7522w-user-guide/specifications?guid=guid-ba3a8be2-9f0e-42ef-9a22-8d67d1682755&lang=en-us)列出笔尖 AES 1.0/2.0 与 WGP。现有平板 8 驱动源码没有证明触控芯片固件能解码这些信号。
- 同系列 Novatek 芯片、蓝牙配对成功，或系统注册了笔输入节点，都不足以证明某支笔能正常书写。

## 文件

- [实验补丁](patches/maxmipad-yupei-experiment.patch)：基于 MaxMiPad 上游提交 `fdd7ecb`；在 `yupei` 上保留系统原有监听器方法，只允许选择驱动源码中明确处理的连接值 `3/8`，并记录 mode 20 的返回值。**补丁未经构建或真机验证。**
- [修改后的两个 Kotlin 文件](source/MaxMiPad/)：便于直接阅读代码；若要移植到上游项目，请优先使用补丁并核对目标版本。
- [Pad 8 实验说明](research/PAD8_EXPERIMENT.md)：补丁作用与局限。
- [PN7522W 研究](research/PN7522W_PAD8.md)：戴尔笔的协议和实测判据。
- [灵感触控笔二代研究](research/SMART_PEN_2_PAD8.md)：平板 6 与 8 的驱动差异。

补丁依赖 MaxMiPad 的完整上游源码及其 `blockmiui` 子模块。本仓库未包含系统固件、内核模块、个人设备日志或私有数据。不要把平板 6 的触控固件直接刷到平板 8。

## 验证状态

仅完成源码对照和 `git diff --check`。由于没有目标触控笔、Android SDK 和完整子模块，未生成 APK，也没有在平板上安装补丁。决定能否继续适配的下一项证据是目标笔靠近屏幕时是否出现原始笔尖位置和压感事件。

MaxMiPad 原项目按 GPL-3.0 授权；本仓库中的派生补丁与研究资料按 GPL-3.0 发布。

