# PN7522W on Xiaomi Pad 8: evidence gate

Target: Dell PN7522W only, Xiaomi Pad 8 (`yupei`, HyperOS 4). This is an experiment plan, not a compatibility claim or an installable release.

## Known facts

- Dell specifies AES 1.0/2.0 and WGP for the pen tip, plus Bluetooth 5.1. See [Dell specifications](https://www.dell.com/support/manuals/en-ph/dell-ap-pn7522w/dell-ap-pn7522w-user-guide/specifications?guid=guid-ba3a8be2-9f0e-42ef-9a22-8d67d1682755&lang=en-us).
- Dell says the pen must first be activated by connecting it with USB-C. See [Dell setup guide](https://www.dell.com/support/manuals/en-uk/dell-ap-pn7522w/dell-ap-pn7522w-user-guide/connecting-and-charging-your-pen?guid=guid-57f4fad5-560f-4266-9c37-ebf86e852ff3&lang=en-us).
- The Pad 8's loaded touch driver is `nt36532_touch`; string evidence for MiPP does not prove AES/WGP support. MiPP and MPP are listed separately by [Goodix](https://www.goodix.com/en/product/touch/active_stylus_driver_chip).
- The existing MaxMiPad Pad 8 branch can request mode 20 while preserving HyperOS 4 listener methods. It cannot decode an AES/WGP pen signal in software if the panel controller never reports one.
- Xiaomi's [Pad 8 touch driver source](https://github.com/MiCode/vendor_xiaomi_proprietary_touch-driver/blob/yupei-w-oss/p81/nt36532/nt36xxx.c) shows that mode 20 handles generations 3 and 8 as connection events. Generations 1/2 set a shielding flag. The default value 2 from older MaxMiPad builds must therefore not be forced on the Pad 8.
- The same source sets the pen switch to named modes `M80P`, `N83P`, and `P81C` (values 1, 2, and 3); these are pen family selectors, not documented AES/WGP decoders. The source contains no demonstrated AES/WGP mode.

## Decision test when the physical pen is available

1. Fully charge and activate PN7522W according to Dell's guide. Record the tablet's raw input device list and a short input event capture with the pen away from the screen.
2. Bring the pen close, then touch and vary pressure. Compare raw stylus position, contact, pressure, and tilt events against the baseline. Bluetooth pairing or a button event alone is not evidence of writing compatibility.
3. If raw stylus events already exist, adapt Android input classification and app handling as needed. If events appear only after the optional MaxMiPad mode 20 request, retain a Pad 8-specific, reversible hook and verify stability over detach, sleep, and reboot.
4. If neither baseline nor the guarded mode request produces pen-tip events, stop Java/LSPosed work. Confirming AES/WGP support would then require controller firmware or vendor documentation; the current module cannot make this pen universally compatible with Pad 8 panels.

No firmware flashing, kernel module replacement, `/proc/nvt_pen_switch` writes, or automatic mode/version sweep belongs in this first test.

## Current status

No PN7522W input trace has been captured. The code branch is unbuilt and untested because the local checkout lacks the `blockmiui` submodule and Android SDK. It should not be distributed as a working PN7522W adaptation.

