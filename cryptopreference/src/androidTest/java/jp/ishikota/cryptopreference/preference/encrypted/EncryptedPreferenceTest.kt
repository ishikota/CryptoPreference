package jp.ishikota.cryptopreference.preference.encrypted

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import jp.ishikota.cryptopreference.helper.TestCipherLogic
import jp.ishikota.cryptopreference.preference.encrypted.encoder.Base64Encoder
import jp.ishikota.cryptopreference.preference.plain.PlainPreference
import jp.ishikota.cryptopreference.preference.plain.PlainPreferenceFactory
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptedPreferenceTest {

    private lateinit var encryptedPreference: EncryptedPreference

    private lateinit var plainPreference: PlainPreference

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val cipher = TestCipherLogic()
        plainPreference = PlainPreferenceFactory().create(appContext, debugMode = true)
        val byteArrayEncoder =  Base64Encoder()
        encryptedPreference = EncryptedPreferenceImpl(cipher, plainPreference, byteArrayEncoder)
    }

    @After
    fun tearUp() {
        plainPreference.clear()
    }

    @Test
    fun testSaveAndGetString() {
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
