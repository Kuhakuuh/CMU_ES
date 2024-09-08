package com.example.escmu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

data class WindowSize(
    val width:WindowType,
    val height:WindowType
)

/**
 * Compact -> Smartphone , Medium -> Landscape mode , Expanded -> Tablet or higher
 */
enum class WindowType {Compact,Medium,Expanded}

/**
 * Calculate and remember the screen size(width and height)
 */
@Composable
fun rememberWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    val screenWidth by remember(key1 = configuration) {
        mutableStateOf(configuration.screenWidthDp)
    }
    val screenHeight by remember(key1 = configuration) {
        mutableStateOf(configuration.screenHeightDp)
    }

    return WindowSize(
        width = getScreenWidth(screenWidth),
        height = getScreenHeight(screenHeight)
    )
}

/**
 * Assign the type  of the screen width
 */
fun getScreenWidth(width: Int): WindowType = when {
    width < 600 -> WindowType.Compact
    width < 840 -> WindowType.Medium
    else -> WindowType.Expanded
}

/**
 * Assign the type of the screen height
 */
fun getScreenHeight(height: Int): WindowType = when {
    height < 480 -> WindowType.Compact
    height < 900 -> WindowType.Medium
    else -> WindowType.Expanded
}