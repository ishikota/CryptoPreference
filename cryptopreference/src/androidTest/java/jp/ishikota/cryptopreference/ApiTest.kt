package jp.ishikota.cryptopreference

import android.content.Context
import android.support.test.InstrumentationRegistry
import junit.framework.Assert.assertEquals
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

        assertEquals("FUGA", encryptedPref.getPrivateString("HOGE"))
    }

    private fun cleanupPreference() {
        val context = InstrumentationRegistry.getTargetContext()
        val pref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        pref.all.keys.forEach { pref.edit().remove(it).apply() }
    }
}
