package jp.ishikota.cryptopreference.cipher

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import jp.ishikota.cryptopreference.helper.TestKeyContainer
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.preference.plain.PlainPreference
import jp.ishikota.cryptopreference.preference.plain.PlainPreferenceImpl
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CipherLogicTest {

    private lateinit var cipherLogic: CipherLogic

    private lateinit var plainPreference: PlainPreference

    private lateinit var keyContainer: SecretKeyContainer

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getTargetContext()
        plainPreference = PlainPreferenceImpl(appContext)
        keyContainer = TestKeyContainer(listOf(ALIAS_1, ALIAS_2))
        cipherLogic = CipherLogicImpl(plainPreference, keyContainer)
    }

    @After
    fun tearUp() {
        plainPreference.clear()
        keyContainer.getAliases().forEach {
            keyContainer.deleteKey(it)
        }
    }

    @Test
    fun testEncryptAndDecrypt() {
        val plainText = "text to encrypt"
        val encrypted = cipherLogic.encrypt(ALIAS_1, plainText)
        val decrypted = cipherLogic.decrypt(ALIAS_1, encrypted)
        assertEquals(plainText, decrypted)
    }

    @Test
    fun testEncryptEmptyString() {
        val emptyText = ""
        val encrypted = cipherLogic.encrypt(ALIAS_1, emptyText)
        val decrypted = cipherLogic.decrypt(ALIAS_1, encrypted)
        assertEquals(emptyText, decrypted)
    }

    @Test(expected = CipherLogicException::class)
    fun testDecryptWithoutEntry() {
        cipherLogic.decrypt(ALIAS_1, byteArrayOf())
    }

    companion object {

        private const val ALIAS_1 = "alias_1"

        private const val ALIAS_2 = "alias_2"

    }
}
