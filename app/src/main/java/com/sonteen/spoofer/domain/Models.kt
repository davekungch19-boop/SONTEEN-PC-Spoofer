package com.sonteen.spoofer.domain

data class DeviceInfo(
    val model: String, val manufacturer: String, val brand: String, val device: String,
    val hardware: String, val androidVersion: String, val sdk: Int, val abi: String,
    val fingerprint: String
)

data class SystemProp(val key: String, val value: String)

data class BackupInfo(val exists: Boolean = false, val createdAt: Long = 0L, val count: Int = 0)

enum class LogLevel { INFO, SUCCESS, WARN, ERROR }

data class LogEntry(val time: String, val level: LogLevel, val message: String)

enum class RootState { CHECKING, GRANTED, DENIED }
