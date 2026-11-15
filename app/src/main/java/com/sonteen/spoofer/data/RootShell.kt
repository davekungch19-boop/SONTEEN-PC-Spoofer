package com.sonteen.spoofer.data

object RootShell {
    data class Result(val ok: Boolean, val out: String, val err: String)

    fun exec(cmd: String): Result = try {
        val p = ProcessBuilder("su", "-c", cmd).start()
        val out = p.inputStream.bufferedReader().readText()
        val err = p.errorStream.bufferedReader().readText()
        Result(p.waitFor() == 0, out.trim(), err.trim())
    } catch (e: Exception) {
        Result(false, "", e.message ?: "su not available")
    }

    fun isRooted(): Boolean = exec("id").let { it.ok && it.out.contains("uid=0") }

    fun getProp(key: String): String = try {
        ProcessBuilder("getprop", key).start().inputStream.bufferedReader().readText().trim()
    } catch (e: Exception) { "" }
}
