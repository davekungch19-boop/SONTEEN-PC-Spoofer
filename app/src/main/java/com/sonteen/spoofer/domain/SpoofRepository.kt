package com.sonteen.spoofer.domain

/** UI talks only to this interface. Implementation lives in the data layer. */
interface SpoofRepository {
    suspend fun checkRoot(): Boolean
    fun deviceInfo(): DeviceInfo
    fun readProps(): List<SystemProp>
    fun isActive(): Boolean
    fun backupInfo(): BackupInfo
    suspend fun enable(): Result<Int>
    suspend fun disable(): Result<Int>
    suspend fun backup(): Result<Int>
    suspend fun restore(): Result<Int>
}
