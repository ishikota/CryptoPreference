package jp.ishikota.cryptopreference.keycontainer

import android.support.test.runner.AndroidJUnit4
import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keycontainer.compat.SecretKeyContainerCompat
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecretKeyContainerCompatTest {

    private lateinit var keyContainer: SecretKeyContainerCompat

    private val AES = Algorithm.AES

    private val CBC = BlockMode.CBC

    private val PKCS7 = Padding.PKCS7

    @Before
    fun setup() {
        keyContainer = SecretKeyContainerCompat(AES_KEY_SECRET.toByteArray())
    }

    @Test
    fun testGenerateNewKey() {
        val secretKey = keyContainer.getOrGenerateNewKey(ALIAS, AES, CBC, PKCS7)
        assertEquals("AES", secretKey.algorithm)
    }

    companion object {

        private const val AES_KEY_SECRET = "abcdefghijklmnop"

        private const val ALIAS = "alias"

    }
}
