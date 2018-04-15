package jp.ishikota.cryptopreference.cryptor

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
class CipherCryptorTest {

    private lateinit var plainPreference: PlainPreference

    private val androidKeystoreContainer = AndroidKeyStoreContainer()

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
        androidKeystoreContainer.deleteKey(ALIAS, AES, CBC, PKCS7)
        plainPreference.clear()
    }

    @Test
    fun encryptAndDecryptByAESWithAndroidKeyStoreBackend() {
        val plain = "some text to encrypt."
        val criptor = genCipher(AndroidKeyStoreContainer())
        val encrypted = criptor.encrypt(ALIAS, plain)
        val decrypted = criptor.decrypt(ALIAS, encrypted)
        assertEquals(plain, String(decrypted))
    }

    @Test
    fun encryptAndDecryptByAESWithCompatBackend() {
        val secret16byte = "0123456789123456"
        val plain = "some text to encrypt."
        val criptor = genCipher(SecretKeyContainerCompat(secret16byte.toByteArray()))
        val encrypted = criptor.encrypt(ALIAS, plain)
        val decrypted = criptor.decrypt(ALIAS, encrypted)
        assertEquals(plain, String(decrypted))
    }

    private fun genCipher(secretKeyContainer: SecretKeyContainer) =
        CipherCryptor(secretKeyContainer, plainPreference, AES, CBC, PKCS7)

    companion object {

        private const val ALIAS = "alias"

    }

}
