package com.sonteen.spoofer.ui.theme

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val CardShape = RoundedCornerShape(18.dp)

@Composable
fun SonCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .shadow(8.dp, CardShape, ambientColor = SonColors.Neon.copy(alpha = .25f), spotColor = SonColors.Neon.copy(alpha = .25f))
            .clip(CardShape)
            .background(SonColors.Card)
            .border(1.dp, SonColors.Border, CardShape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

@Composable
fun SectionTitle(icon: ImageVector, text: String, trailing: @Composable (() -> Unit)? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, tint = SonColors.Neon, modifier = Modifier.size(20.dp))
        Text(text, style = MaterialTheme.typography.titleMedium, color = SonColors.TextPrimary, modifier = Modifier.weight(1f))
        trailing?.invoke()
    }
}

@Composable
fun GradientButton(text: String, icon: ImageVector, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier
            .alpha(if (enabled) 1f else .4f)
            .height(50.dp)
            .shadow(if (enabled) 10.dp else 0.dp, shape, ambientColor = SonColors.Pink.copy(alpha = .4f), spotColor = SonColors.Pink.copy(alpha = .4f))
            .clip(shape)
            .background(SonColors.MainGradient)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun NeonOutlineButton(text: String, icon: ImageVector, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier
            .alpha(if (enabled) 1f else .4f)
            .height(50.dp)
            .clip(shape)
            .background(SonColors.Surface)
            .border(1.dp, SonColors.Neon.copy(alpha = .6f), shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = SonColors.Neon, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = SonColors.TextPrimary, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Row(
        Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = .15f))
            .border(1.dp, color.copy(alpha = .5f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(7.dp).clip(RoundedCornerShape(50)).background(color))
        Spacer(Modifier.width(6.dp))
        Text(text, color = color, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = SonColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.width(12.dp))
        Text(value.ifBlank { "-" }, color = SonColors.TextPrimary, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f, fill = false), maxLines = 2)
    }
}
