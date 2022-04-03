package org.ossiaustria.lib.nfc

data class NfcTagData(
    val success: Boolean,
    val tagId: String?,
    val message: String? = null,
    val type: String? = null,
    val error: String? = null
) {
    companion object {
        fun extractNfcId(byteArray: ByteArray, maxLen: Int = byteArray.size): String {
            val uidStringBuffer = StringBuffer()
            for (i in 0 until maxLen) {
                val hexValue = String.format("%02X:", byteArray[i])
                uidStringBuffer.append(hexValue)
            }
            return uidStringBuffer.removeSuffix(":").toString()
        }
    }
}