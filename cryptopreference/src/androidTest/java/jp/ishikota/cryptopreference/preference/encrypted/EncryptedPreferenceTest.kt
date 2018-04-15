package jp.ishikota.cryptopreference.preference.encrypted

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.keycontainer.androidkeystore.AndroidKeyStoreContainer
import jp.ishikota.cryptopreference.keycontainer.compat.SecretKeyContainerCompat
import jp.ishikota.cryptopreference.preference.plain.PlainPreference
import jp.ishikota.cryptopreference.preference.plain.PlainPreferenceFactory
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptedPreferenceTest {

    private lateinit var plainPreference: PlainPreference

    private val androidKeystoreContainer = AndroidKeyStoreContainer()

    private val encryptedPreferenceFactory = EncryptedPreferenceFactory()

    private val AES = SecretKeyContainer.Algorithm.AES

    private val CBC = SecretKeyContainer.BlockMode.CBC

    private val PKCS7 = SecretKeyContainer.Padding.PKCS7

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getTargetContext()
        plainPreference = PlainPreferenceFactory().create(appContext, debugMode = true)
    }

    @After
    fun tearDown() {
        androidKeystoreContainer.clearKeys()
        plainPreference.clear()
    }

    @Test
    fun testSaveAndGetPrivateStringWithAndroidKeystoreContainerBackend() {
        val textToSave = "This is very important string."
        val encryptedPref = genAndroidKeystoreBackendPref()
        encryptedPref.savePrivateString(KEY_PREF, textToSave)
        val fetched = encryptedPref.getPrivateString(KEY_PREF)
        assertEquals(textToSave, fetched)
    }

    @Test
    fun testSaveAndGetPrivateStringWithCompatKeyContainerBackend() {
        val textToSave = "This is very important string."
        val encryptedPref = genCompatKeystoreBackendPref()
        encryptedPref.savePrivateString(KEY_PREF, textToSave)
        val fetched = encryptedPref.getPrivateString(KEY_PREF)
        assertEquals(textToSave, fetched)
    }

    private fun genAndroidKeystoreBackendPref() = encryptedPreferenceFactory.create(
        androidKeystoreContainer, plainPreference, AES, CBC, PKCS7
    )

    private fun genCompatKeystoreBackendPref() = encryptedPreferenceFactory.create(
        SecretKeyContainerCompat(SECRET_16_BYTE), plainPreference, AES, CBC, PKCS7
    )

    companion object {

        private val KEY_PREF = "important_string_key"

        private val SECRET_16_BYTE = "0123456789123456".toByteArray()

    }

}