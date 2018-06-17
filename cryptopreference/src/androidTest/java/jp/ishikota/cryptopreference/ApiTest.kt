package jp.ishikota.cryptopreference

import android.content.Context
import android.support.test.InstrumentationRegistry
import jp.ishikota.cryptopreference.obfuscator.Sha256Obfuscator
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Test

class ApiTest {

    private val preferenceName = "CryptoPreference"

    @After
    fun tearDown() {
        cleanupPreference()
        CryptoPreference.Initializer.apply {
            preferenceName = null
            secretKeyForCompat = null
            debugMode = false
            keyObfuscator = Sha256Obfuscator()
        }
    }

    @Test(expected = InitializationException::class)
    fun callCreateWithoutInitialization() {
        val context = InstrumentationRegistry.getTargetContext()
        CryptoPreference.create(context, Algorithm.AES, BlockMode.CBC, Padding.PKCS7)
    }

    @Test
    fun callCreate() {
        val context = InstrumentationRegistry.getTargetContext()

        CryptoPreference.Initializer.also {
            it.preferenceName = preferenceName
            it.secretKeyForCompat = "1234567890123456".toByteArray()
        }
        val encryptedPref = CryptoPreference.create(context, Algorithm.AES, BlockMode.CBC, Padding.PKCS7)
        encryptedPref.savePrivateString("HOGE", "FUGA")

        assertEquals("FUGA", encryptedPref.getPrivateString("HOGE", ""))
    }

    @Test
    fun decryptWithoutEntry() {
        val context = InstrumentationRegistry.getTargetContext()

        CryptoPreference.Initializer.also {
            it.preferenceName = preferenceName
            it.secretKeyForCompat = "1234567890123456".toByteArray()
        }

        val encryptedPref = CryptoPreference.create(context, Algorithm.AES, BlockMode.CBC, Padding.PKCS7)
        val value = encryptedPref.getPrivateString("HOGE", "default")
        assertEquals("default", value)
    }

    @Test
    fun overrideKeyObfuscationLogic() {
        val context = InstrumentationRegistry.getTargetContext()

        // replace obfuscator
        val reverseObfuscator = object: KeyObfuscator {
            override fun obfuscate(rawKey: String): String = rawKey.reversed()
        }
        CryptoPreference.Initializer.also {
            it.preferenceName = preferenceName
            it.secretKeyForCompat = "1234567890123456".toByteArray()
            it.keyObfuscator = reverseObfuscator
        }

        val encryptedPref = CryptoPreference.create(context, Algorithm.AES, BlockMode.CBC, Padding.PKCS7)
        encryptedPref.savePrivateString("HOGE", "FUGA")

        val rawPreference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        assertTrue(rawPreference.contains("HOGE".reversed()))
    }

    private fun cleanupPreference() {
        val context = InstrumentationRegistry.getTargetContext()
        val pref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        pref.all.keys.forEach { pref.edit().remove(it).apply() }
    }
}
