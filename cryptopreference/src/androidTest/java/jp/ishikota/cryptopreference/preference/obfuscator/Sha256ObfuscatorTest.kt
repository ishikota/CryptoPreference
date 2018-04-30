package jp.ishikota.cryptopreference.preference.obfuscator

import junit.framework.Assert.assertEquals
import org.junit.Test

class Sha256ObfuscatorTest {

    private val obfuscator = Sha256Obfuscator()

    @Test
    fun obfuscateString() {
        val row = "key_access_token"
        val expected = "A3ABA7CC38234CAD5F2C81E6FDCAC07282B67CBCF69B5248E0F8312C9D807B34".toLowerCase()
        assertEquals(expected, obfuscator.obfuscate(row))
    }

}
