package com.sonteen.spoofer.ui.main

import com.sonteen.spoofer.domain.*

data class MainUiState(
    val root: RootState = RootState.CHECKING,
    val device: DeviceInfo? = null,
    val props: List<SystemProp> = emptyList(),
    val active: Boolean = false,
    val backup: BackupInfo = BackupInfo(),
    val busy: Boolean = false,
    val log: List<LogEntry> = emptyList()
)
