package net.catten.ninekeyhertz.utils

import android.os.Build
import android.view.WindowManager.LayoutParams

/* Window Manager Layout Parameter Builder */

@Suppress("FunctionName")
fun WindowManagerLayoutParameters(
    layoutParameterBuilder : WindowManagerLayoutParametersBuilderContext.() -> Unit
) = WindowManagerLayoutParametersBuilderContext().apply(layoutParameterBuilder).build()

class WindowManagerLayoutParametersBuilderContext internal constructor(private val params : LayoutParams = LayoutParams()) {
    fun layoutParameters(modifications : LayoutParams.() -> Unit) = params.apply(modifications)

    internal fun build() = params
}

private typealias WMLPBC = WindowManagerLayoutParametersBuilderContext

fun WMLPBC.fullScreen() = layoutParameters {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        width = LayoutParams.FLAG_FULLSCREEN
        height = LayoutParams.FLAG_FULLSCREEN
    }
}

fun WMLPBC.fullWidth() = layoutParameters { width = LayoutParams.MATCH_PARENT }
fun WMLPBC.fullHeight() = layoutParameters { height = LayoutParams.MATCH_PARENT }

fun WMLPBC.tightHeight() = layoutParameters { height = LayoutParams.WRAP_CONTENT }

fun WMLPBC.typeOverlay() = layoutParameters {
    type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) LayoutParams.TYPE_APPLICATION_OVERLAY
    else LayoutParams.TYPE_SYSTEM_ALERT
}

fun WMLPBC.useFlags(vararg windowFlags : Int) = layoutParameters {
    flags = windowFlags.reduce { acc, i -> acc or i }
}