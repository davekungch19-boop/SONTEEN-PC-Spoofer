package com.sonteen.spoofer.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sonteen.spoofer.domain.*
import com.sonteen.spoofer.ui.theme.*
import java.text.DateFormat
import java.util.Date

@Composable
fun MainScreen(vm: MainViewModel = viewModel()) {
    val s by vm.state.collectAsStateWithLifecycle()
    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(SonColors.Background, Color(0xFF130B22), SonColors.Background))
        )
    ) {
        LazyColumn(
            Modifier.widthIn(max = 640.dp).fillMaxHeight().align(Alignment.TopCenter)
                .statusBarsPadding().navigationBarsPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Header() }
            item { RootCard(s.root, vm::refresh) }
            item { DeviceCard(s.device) }
            item { ControlCard(s, vm::toggle) }
            item { BackupCard(s, vm::backup, vm::restore) }
            item { PropsCard(s.props) }
            item { LogCard(s.log, vm::clearLog) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            Modifier.size(52.dp).background(SonColors.MainGradient, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Hexagon, null, tint = Color.White, modifier = Modifier.size(30.dp)) }
        Column {
            Text("SONTEEN", style = MaterialTheme.typography.headlineMedium.copy(brush = SonColors.MainGradient))
            Text("PC Spoofer", color = SonColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun RootCard(root: RootState, onRecheck: () -> Unit) {
    val (txt, col) = when (root) {
        RootState.CHECKING -> "Checking..." to SonColors.Warn
        RootState.GRANTED -> "Root Granted" to SonColors.Success
        RootState.DENIED -> "No Root" to SonColors.Error
    }
    SonCard {
        SectionTitle(Icons.Filled.AdminPanelSettings, "Root Status") { StatusChip(txt, col) }
        if (root == RootState.DENIED)
            Text("This device is not rooted or root permission was denied. Features are disabled.",
                color = SonColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
        NeonOutlineButton("Re-check Root", Icons.Filled.Refresh, Modifier.fillMaxWidth(), onClick = onRecheck)
    }
}

@Composable
private fun DeviceCard(d: DeviceInfo?) = SonCard {
    SectionTitle(Icons.Filled.PhoneAndroid, "Device Information")
    if (d != null) {
        InfoRow("Model", d.model); InfoRow("Manufacturer", d.manufacturer); InfoRow("Brand", d.brand)
        InfoRow("Device", d.device); InfoRow("Hardware", d.hardware)
        InfoRow("Android", "${d.androidVersion} (API ${d.sdk})"); InfoRow("ABI", d.abi)
    }
}

@Composable
private fun ControlCard(s: MainUiState, onToggle: (Boolean) -> Unit) = SonCard {
    SectionTitle(Icons.Filled.PowerSettingsNew, "Emulator Profile") {
        when {
            s.busy -> StatusChip("Working", SonColors.Warn)
            s.active -> StatusChip("Active", SonColors.Success)
            else -> StatusChip("Inactive", SonColors.TextSecondary)
        }
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Enable function", color = SonColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
            Text(if (s.active) "Device properties are set to the emulator profile"
            else "Original device properties in use", color = SonColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = s.active, onCheckedChange = onToggle,
            enabled = s.root == RootState.GRANTED && !s.busy,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White, checkedTrackColor = SonColors.Neon,
                uncheckedThumbColor = SonColors.TextSecondary, uncheckedTrackColor = SonColors.Surface
            )
        )
    }
    if (s.busy) LinearProgressIndicator(Modifier.fillMaxWidth(), color = SonColors.Pink, trackColor = SonColors.Surface)
}

@Composable
private fun BackupCard(s: MainUiState, onBackup: () -> Unit, onRestore: () -> Unit) = SonCard {
    SectionTitle(Icons.Filled.Backup, "Backup & Restore")
    Text(
        if (s.backup.exists) "Last backup: ${DateFormat.getDateTimeInstance().format(Date(s.backup.createdAt))} (${s.backup.count} props)"
        else "No backup yet", color = SonColors.TextSecondary, style = MaterialTheme.typography.bodySmall
    )
    val enabled = s.root == RootState.GRANTED && !s.busy
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        GradientButton("Backup", Icons.Filled.Save, Modifier.weight(1f), enabled && !s.active, onBackup)
        NeonOutlineButton("Restore", Icons.Filled.Restore, Modifier.weight(1f), enabled && s.backup.exists, onRestore)
    }
}

@Composable
private fun PropsCard(props: List<SystemProp>) = SonCard {
    SectionTitle(Icons.Filled.Memory, "System Properties")
    props.forEach {
        Column {
            Text(it.key, color = SonColors.Neon, style = MaterialTheme.typography.labelSmall)
            Text(it.value.ifBlank { "(empty)" }, color = SonColors.TextPrimary, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun LogCard(log: List<LogEntry>, onClear: () -> Unit) = SonCard {
    SectionTitle(Icons.Filled.Terminal, "Activity Log") {
        IconButton(onClick = onClear, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Filled.DeleteSweep, "Clear", tint = SonColors.TextSecondary)
        }
    }
    if (log.isEmpty()) Text("No activity", color = SonColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
    log.take(30).forEach {
        val c = when (it.level) {
            LogLevel.INFO -> SonColors.TextSecondary; LogLevel.SUCCESS -> SonColors.Success
            LogLevel.WARN -> SonColors.Warn; LogLevel.ERROR -> SonColors.Error
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(it.time, color = SonColors.TextSecondary, style = MaterialTheme.typography.labelSmall)
            Text(it.message, color = c, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
        }
    }
}
