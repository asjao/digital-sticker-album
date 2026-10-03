package com.example.stickeralbum.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(

    primary = Gold,
    onPrimary = DarkText,

    primaryContainer = ForestGreen,
    onPrimaryContainer = Color.White,

    secondary = EmeraldGreen,
    onSecondary = Color.White,

    background = DarkGreenBackground,
    onBackground = Color.White,

    surface = DeepGreen,
    onSurface = Color.White,

    surfaceVariant = ForestGreen,
    onSurfaceVariant = Color.White
)
private val LightColorScheme = lightColorScheme(

    primary = DeepGreen,
    onPrimary = Color.White,

    primaryContainer = ForestGreen,
    onPrimaryContainer = Color.White,

    secondary = Gold,
    onSecondary = DarkText,

    secondaryContainer = LightGold,
    onSecondaryContainer = DarkText,

    background = Cream,
    onBackground = DarkText,

    surface = WarmWhite,
    onSurface = DarkText,

    surfaceVariant = Color(0xFFE9E6DD),
    onSurfaceVariant = SoftGray
)
@Composable
fun StickerAlbumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}