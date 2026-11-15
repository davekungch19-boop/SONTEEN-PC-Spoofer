package com.sonteen.spoofer.data

import android.content.Context
import com.sonteen.spoofer.domain.BackupInfo
import org.json.JSONObject

class BackupStore(context: Context) {
    private val prefs = context.getSharedPreferences("sonteen", Context.MODE_PRIVATE)

    var active: Boolean
        get() = prefs.getBoolean("active", false)
        set(v) = prefs.edit().putBoolean("active", v).apply()

    fun save(values: Map<String, String>) {
        val o = JSONObject()
        values.forEach { (k, v) -> o.put(k, v) }
        prefs.edit().putString("backup", o.toString())
            .putLong("backup_time", System.currentTimeMillis()).apply()
    }

    fun load(): Map<String, String> {
        val raw = prefs.getString("backup", null) ?: return emptyMap()
        val o = JSONObject(raw)
        return o.keys().asSequence().associateWith { o.getString(it) }
    }

    fun info(): BackupInfo {
        val m = load()
        return BackupInfo(m.isNotEmpty(), prefs.getLong("backup_time", 0L), m.size)
    }
}
