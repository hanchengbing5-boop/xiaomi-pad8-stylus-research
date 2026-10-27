# Pad 8 stylus experiment

This source branch adds a guarded `yupei` path to the existing MaxMiPad stylus toggle. It has **not** been built, installed, or validated with a pen.

When the user enables the existing stylus toggle and scopes the module to the Android system process, the Pad 8 path only runs after `MiuiStylusDeviceListener` is constructed. It requests touch mode 20 with `0x10 | version` and records the return value in the Android log under `MaxMiPad`. The original listener methods remain active, including the device list and touch-film callbacks that HyperOS 4 uses. The toggle remains off by default.

The Pad 8 driver source in Xiaomi's [`yupei-w-oss` touch-driver branch](https://github.com/MiCode/vendor_xiaomi_proprietary_touch-driver/blob/yupei-w-oss/p81/nt36532/nt36xxx.c) handles generation 3 as M80P and generation 8 as P81C. Generations 1/2 set a shielding flag, while other generations have no matching connection case. The Pad 8 hook therefore rejects every value except 3 and 8; the UI exposes 8 on `yupei`. Its original default of 2 now performs no mode request on Pad 8. Neither 3 nor 8 is confirmed for Dell PN7522W. A Java/HAL request can succeed without the panel recognizing the pen's AES/WGP signal. This change does not add a protocol decoder.

Before treating this as usable, obtain a target pen and compare raw input events before and after enabling the toggle. Check whether the pen produces position, pressure, and tilt events, not merely whether Bluetooth pairs. If it does not, turn the toggle off and remove the module from the Android scope. Do not write to `/proc/nvt_pen_switch` or modify the touch firmware as part of this experiment.

The local checkout currently lacks its `blockmiui` submodule content and an Android SDK. There is no verified APK artifact yet.

