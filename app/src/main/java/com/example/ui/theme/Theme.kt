package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = KundeGold,
    onPrimary = KundeNavyDark,
    primaryContainer = KundeNavyLight,
    onPrimaryContainer = KundeGoldLight,
    secondary = KundeGoldWarm,
    onSecondary = KundeNavyDark,
    background = KundeNavyDark,
    onBackground = Color.White,
    surface = KundeNavyCard,
    onSurface = Color.White,
    surfaceVariant = KundeNavyLight,
    onSurfaceVariant = KundeTextMuted,
    outline = KundeBorderDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = KundeNavy,
    onPrimary = Color.White,
    primaryContainer = KundeNavyLight,
    onPrimaryContainer = Color.White,
    secondary = KundeGoldDark,
    onSecondary = KundeNavyDark,
    secondaryContainer = KundeGoldLight,
    onSecondaryContainer = KundeNavyDark,
    tertiary = KundeGreen,
    background = KundeBackground,
    onBackground = KundeTextPrimary,
    surface = KundeSurface,
    onSurface = KundeTextPrimary,
    surfaceVariant = KundeSurfaceVariant,
    onSurfaceVariant = KundeTextSecondary,
    outline = KundeBorder,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve our distinctive African brand identity
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

