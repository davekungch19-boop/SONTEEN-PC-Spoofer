package com.sonteen.spoofer.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sonteen.spoofer.data.SpoofRepositoryImpl
import com.sonteen.spoofer.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo: SpoofRepository = SpoofRepositoryImpl(app)
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()
    private val fmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    init { log(LogLevel.INFO, "SONTEEN PC Spoofer started"); refresh() }

    private fun log(level: LogLevel, msg: String) = _state.update {
        it.copy(log = (listOf(LogEntry(fmt.format(Date()), level, msg)) + it.log).take(100))
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(root = RootState.CHECKING) }
            val rooted = withContext(Dispatchers.IO) { repo.checkRoot() }
            val dev = withContext(Dispatchers.IO) { repo.deviceInfo() }
            val props = withContext(Dispatchers.IO) { repo.readProps() }
            _state.update {
                it.copy(root = if (rooted) RootState.GRANTED else RootState.DENIED, device = dev,
                    props = props, active = repo.isActive(), backup = repo.backupInfo())
            }
            if (rooted) log(LogLevel.SUCCESS, "Root access granted")
            else log(LogLevel.WARN, "Root access denied / not found")
        }
    }

    private fun run(label: String, block: suspend () -> Result<Int>) {
        val s = _state.value
        if (s.busy) return
        if (s.root != RootState.GRANTED) { log(LogLevel.ERROR, "$label failed: root required"); return }
        viewModelScope.launch {
            _state.update { it.copy(busy = true) }
            log(LogLevel.INFO, "$label...")
            val r = withContext(Dispatchers.IO) { block() }
            r.onSuccess { log(LogLevel.SUCCESS, "$label done ($it properties)") }
                .onFailure { log(LogLevel.ERROR, "$label failed: ${it.message}") }
            val props = withContext(Dispatchers.IO) { repo.readProps() }
            _state.update { it.copy(busy = false, props = props, active = repo.isActive(), backup = repo.backupInfo()) }
        }
    }

    fun toggle(on: Boolean) {
        if (on) run("Enable emulator profile") { repo.enable() } else run("Disable (restore)") { repo.disable() }
    }
    fun backup() = run("Backup") { repo.backup() }
    fun restore() = run("Restore") { repo.restore() }
    fun clearLog() = _state.update { it.copy(log = emptyList()) }
}
