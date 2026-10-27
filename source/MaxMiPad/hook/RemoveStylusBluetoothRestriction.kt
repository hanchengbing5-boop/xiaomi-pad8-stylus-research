package com.yifeplayte.maxmipadinput.hook.hooks.singlepackage.android

import android.os.Build
import android.util.Log
import com.github.kyuubiran.ezxhelper.ClassUtils.invokeStaticMethodBestMatch
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.ClassUtils.loadFirstClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import com.github.kyuubiran.ezxhelper.ObjectUtils.invokeMethodBestMatch
import com.yifeplayte.maxmipadinput.hook.hooks.BaseHook
import com.yifeplayte.maxmipadinput.hook.utils.XSharedPreferences.getString

object RemoveStylusBluetoothRestriction : BaseHook() {
    override val key = "remove_stylus_bluetooth_restriction"
    override fun hook() {
        val isNew = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        val clazzMiuiStylusDeviceListener = loadFirstClass(
            "com.miui.server.input.stylus.MiuiStylusDeviceListener",
            "com.miui.server.stylus.MiuiStylusDeviceListener"
        )
        if (Build.DEVICE == "yupei") {
            // Keep HyperOS 4's device list and touch-film callbacks intact.
            clazzMiuiStylusDeviceListener.declaredConstructors.createHooks {
                after {
                    applyPad8Mode()
                }
            }
            return
        }
        clazzMiuiStylusDeviceListener.declaredConstructors.createHooks {
            after {
                setTouchModeStylusEnable(isNew)
            }
        }
        clazzMiuiStylusDeviceListener.declaredMethods.createHooks {
            replace {
                setTouchModeStylusEnable(isNew)
            }
        }
    }

    private fun applyPad8Mode() {
        val version = getString("remove_stylus_bluetooth_restriction_driver_version", "2")
            .toIntOrNull()
        // The yupei-w-oss driver only handles generation 3 (M80P) and 8 (P81C)
        // as active pen connections. Generations 1/2 set pen_shield_flag instead.
        if (version != 3 && version != 8) {
            Log.w("MaxMiPad", "Pad 8 stylus mode skipped: unsupported generation=$version")
            return
        }
        try {
            val touchFeature = invokeStaticMethodBestMatch(
                loadClass("miui.util.ITouchFeature"), "getInstance"
            ) ?: return
            val result = invokeMethodBestMatch(touchFeature, "setTouchMode", null, 0, 20, 0x10 or version)
            Log.i("MaxMiPad", "Pad 8 stylus mode request version=$version result=$result")
        } catch (error: Throwable) {
            Log.e("MaxMiPad", "Pad 8 stylus mode request failed", error)
        }
    }

    private fun setTouchModeStylusEnable(isNew: Boolean) {
        val driverVersion =
            getString("remove_stylus_bluetooth_restriction_driver_version", "2").toInt()
        val flag: Int = if (isNew) (0x10 or driverVersion) else 1
        val instanceITouchFeature =
            invokeStaticMethodBestMatch(loadClass("miui.util.ITouchFeature"), "getInstance")!!
        invokeMethodBestMatch(
            instanceITouchFeature,
            "setTouchMode",
            null,
            0, 20, flag
        )
    }
}

