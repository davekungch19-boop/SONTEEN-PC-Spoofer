package com.sonteen.spoofer.data

import android.content.Context
import android.os.Build
import com.sonteen.spoofer.domain.*

class SpoofRepositoryImpl(context: Context) : SpoofRepository {
    private val store = BackupStore(context.applicationContext)
    private val keyRegex = Regex("^[A-Za-z0-9._-]+$")

    override suspend fun checkRoot() = RootShell.isRooted()

    override fun deviceInfo() = DeviceInfo(
        model = Build.MODEL, manufacturer = Build.MANUFACTURER, brand = Build.BRAND,
        device = Build.DEVICE, hardware = Build.HARDWARE,
        androidVersion = Build.VERSION.RELEASE, sdk = Build.VERSION.SDK_INT,
        abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "-", fingerprint = Build.FINGERPRINT
    )

    override fun readProps() = EmulatorProfile.props.keys.map { SystemProp(it, RootShell.getProp(it)) }
    override fun isActive() = store.active
    override fun backupInfo() = store.info()

    private fun q(v: String) = "'" + v.replace("'", "'\\''") + "'"

    private fun setProp(key: String, value: String): Boolean {
        if (!keyRegex.matches(key)) return false
        val cmd = if (value.isEmpty())
            "resetprop --delete $key 2>/dev/null || true"
        else
            "if command -v resetprop >/dev/null 2>&1; then resetprop $key ${q(value)}; else setprop $key ${q(value)}; fi"
        return RootShell.exec(cmd).ok
    }

    private fun applyAll(map: Map<String, String>): Result<Int> {
        val ok = map.count { (k, v) -> setProp(k, v) }
        return if (ok == 0) Result.failure(Exception("No property could be written (root / resetprop required)"))
        else Result.success(ok)
    }

    private fun snapshot() = EmulatorProfile.props.keys.associateWith { RootShell.getProp(it) }

    override suspend fun enable(): Result<Int> {
        if (!store.info().exists) store.save(snapshot())
        return applyAll(EmulatorProfile.props).onSuccess { store.active = true }
    }

    override suspend fun disable() = restore()

    override suspend fun backup(): Result<Int> {
        if (store.active) return Result.failure(Exception("Turn the function off before creating a backup"))
        val s = snapshot(); store.save(s); return Result.success(s.size)
    }

    override suspend fun restore(): Result<Int> {
        val saved = store.load()
        if (saved.isEmpty()) return Result.failure(Exception("No backup found"))
        return applyAll(saved).onSuccess { store.active = false }
    }
}
