package jp.ishikota.cryptopreference.obfuscator

import jp.ishikota.cryptopreference.KeyObfuscator
import java.security.MessageDigest

class Sha256Obfuscator: KeyObfuscator {

    override fun obfuscate(rawKey: String): String {
        val messageDigest = MessageDigest.getInstance(SHA_256)
        messageDigest.update(rawKey.toByteArray())
        return bytesToHex(messageDigest.digest())
    }

    private fun bytesToHex(bytes: ByteArray) = bytes.joinToString("") { String.format("%02x", it) }

    companion object {

        private const val SHA_256 = "SHA-256"

    }
}
