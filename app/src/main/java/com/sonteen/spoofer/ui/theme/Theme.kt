package com.sonteen.spoofer.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object SonColors {
    val Background = Color(0xFF0B0714)
    val Surface = Color(0xFF151020)
    val Card = Color(0xFF20172F)
    val Primary = Color(0xFF8B5CF6)
    val Neon = Color(0xFFA855F7)
    val Pink = Color(0xFFEC4899)
    val TextPrimary = Color(0xFFF8F5FF)
    val TextSecondary = Color(0xFFA69BB8)
    val Success = Color(0xFF34D399)
    val Warn = Color(0xFFFBBF24)
    val Error = Color(0xFFF87171)
    val Border = Primary.copy(alpha = 0.35f)
    val MainGradient = Brush.horizontalGradient(listOf(Primary, Pink))
}

private val scheme = darkColorScheme(
    primary = SonColors.Primary, secondary = SonColors.Pink, tertiary = SonColors.Neon,
    background = SonColors.Background, surface = SonColors.Surface, surfaceVariant = SonColors.Card,
    onPrimary = Color.White, onBackground = SonColors.TextPrimary, onSurface = SonColors.TextPrimary
)

val SonTypography = Typography(
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Black, fontSize = 26.sp, letterSpacing = 3.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 0.5.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 15.sp)
)

@Composable
fun SonteenTheme(content: @Composable () -> Unit) {
    // Always dark, regardless of system setting.
    MaterialTheme(colorScheme = scheme, typography = SonTypography, content = content)
}
