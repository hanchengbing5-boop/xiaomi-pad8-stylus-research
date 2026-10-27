# Xiaomi Smart Pen (2nd generation, M82) on Xiaomi Pad 8

This is offline research for a possible Pad 8 (`yupei`) adaptation. It is not a compatibility claim or a tested module.

## Evidence

- Xiaomi's [Smart Pen 2 quick start guide](https://alsgp0.fds.api.xiaomi.com/gl123/Xiaomi%20Smart%20Pen/M82/_M82_%E6%89%8B%E5%86%99%E7%AC%94_QSG_%E5%9B%BD%E9%99%8511%E8%AF%AD%E7%89%88_A_20230423.pdf) identifies the M82 accessory and says to attach it to a Pad 5/6 magnetic charging edge to pair and charge. Pad 8 charging and pairing are not promised.
- Xiaomi's [accessory compatibility list](https://www.mi.com/global/support/faq/details/KA-544109/) lists Smart Pen 2 for Pad 5/6 and Focus Pen/Focus Pen Pro for Pad 8.
- The [Pad 6 `pipa-t-oss` Novatek driver](https://github.com/MiCode/Xiaomi_Kernel_OpenSource/blob/pipa-t-oss/drivers/input/touchscreen/nt36532/nt36xxx.c) handles low-nibble pen ID 2 as a connected pen and enables the pen input device. Its [header](https://github.com/MiCode/Xiaomi_Kernel_OpenSource/blob/pipa-t-oss/drivers/input/touchscreen/nt36532/nt36xxx.h) names an `m82` firmware image. This supports the inference that the Pad 6 M82 pen is part of this older Novatek path, but does not identify its over-screen electrical protocol.
- The [Pad 8 `yupei-w-oss` Novatek driver](https://github.com/MiCode/vendor_xiaomi_proprietary_touch-driver/blob/yupei-w-oss/p81/nt36532/nt36xxx.c) treats IDs 1/2 as shielding events. It handles ID 3 as M80P and ID 8 as P81C. [Xiaomi's FCC label filing](https://fccid.io/2AFZZMP43C/Label/Label-7494319) identifies M80P as a Focus Pen project. The driver registers pen input devices for M80P/P81C, with no named M82 input path in the examined source.

## Feasible investigation

1. Compare Pad 6 and Pad 8 pen command paths, event formats, and actual firmware images offline. Shared NT36532 branding is not proof that the firmware or panel sensor is interchangeable.
2. If an M82 pen is available, first charge it using a confirmed compatible Pad 5/6 or charging method. Compare Pad 8 raw pen events with the pen away/near/touching, before any module or driver write.
3. If raw pen events exist, a limited software adaptation may map them into Android input and Bluetooth recognition. If no raw events exist, a Java hook alone cannot recover the pen signal.
4. Do not flash Pad 6 touch firmware onto Pad 8: panel geometry, calibration, boot flow, and recovery requirements differ. Investigate firmware capability first; an offline diff is safe, a cross-device flash is not.

Conclusion: M82 is a more focused research target than Dell PN7522W because an older Xiaomi/Novatek driver path exists, but working writing, pressure, charging, and pairing on Pad 8 remain unverified.

