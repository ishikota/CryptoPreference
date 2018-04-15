package jp.ishikota.cryptopreference.integration

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import jp.ishikota.cryptopreference.cipher.CipherLogicImpl
import jp.ishikota.cryptopreference.keycontainer.AndroidKeyStoreContainer
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreference
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreferenceImpl
import jp.ishikota.cryptopreference.preference.encrypted.encoder.Base64Encoder
import jp.ishikota.cryptopreference.preference.plain.PlainPreference
import jp.ishikota.cryptopreference.preference.plain.PlainPreferenceFactory
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntegrationTest {

    private lateinit var plainPreference: PlainPreference

    private lateinit var androidKeyStoreContainer: AndroidKeyStoreContainer

    private lateinit var cipher: CipherLogicImpl

    private lateinit var encryptedPreference: EncryptedPreference

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getTargetContext()
        plainPreference = PlainPreferenceFactory().create(appContext, debugMode = true)
        androidKeyStoreContainer = AndroidKeyStoreContainer()
        cipher = CipherLogicImpl(plainPreference, androidKeyStoreContainer)
        encryptedPreference = EncryptedPreferenceImpl(cipher, plainPreference, Base64Encoder())
    }

    @After
    fun tearDown() {
        plainPreference.clear()
        androidKeyStoreContainer.getAliases().forEach { androidKeyStoreContainer.deleteKey(it) }
    }

    @Test
    fun saveAndGetString() {
        val key = "test"
        val text = "text to save"
        encryptedPreference.saveString(key, text)
        val fetched = encryptedPreference.getString(key)
        assertEquals(text, fetched)
    }

    @Test
    fun testGetStringWithoutSave() {
        val key = "test"
        val fetched = encryptedPreference.getString(key)
        assertEquals("", fetched)
    }

}
