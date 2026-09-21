package com.namratha.focusplanbuilder.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = AndroidGreenDark,
    onPrimary = White,
    secondary = AndroidGreenLight,
    tertiary = AndroidGreenDarkest,
    surfaceVariant = AndroidGreenLight,
    surfaceContainerHighest = AndroidGreenLight
)

private val LightColorScheme = lightColorScheme(
    primary = Purple80,
    onPrimary = Black,
    secondary = PurpleGrey40,
    tertiary = Purple40,
    surfaceVariant = Purple80,
    surfaceContainerHighest = PurpleVeryLight
)

@Composable
fun FocusPlanBuilderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}