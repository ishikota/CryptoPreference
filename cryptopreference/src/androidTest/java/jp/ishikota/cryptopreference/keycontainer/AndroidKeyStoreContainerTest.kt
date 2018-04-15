package jp.ishikota.cryptopreference.keycontainer

import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidKeyStoreContainerTest {

    private lateinit var keyContainer: AndroidKeyStoreContainer

    private val AES = SecretKeyContainer.Algorithm.AES

    private val CBC = SecretKeyContainer.BlockMode.CBC

    private val PKCS7 = SecretKeyContainer.Padding.PKCS7

    @Before
    fun setup() {
        keyContainer = AndroidKeyStoreContainer()
    }

    @After
    fun tearUp() {
        keyContainer.deleteKey(ALIAS_1, AES, CBC, PKCS7)
        keyContainer.deleteKey(ALIAS_2, AES, CBC, PKCS7)
    }

    @Test
    fun testGenerateNewKey() {
        assertFalse(keyContainer.hasKey(ALIAS_1, AES, CBC, PKCS7))
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES, CBC, PKCS7)
        assertTrue(keyContainer.hasKey(ALIAS_1, AES, CBC, PKCS7))
    }

    @Test
    fun testGetSavedKey() {
        val generated = keyContainer.getOrGenerateNewKey(ALIAS_1, AES, CBC, PKCS7)
        val another = keyContainer.getOrGenerateNewKey(ALIAS_2, AES, CBC, PKCS7)
        val saved = keyContainer.getOrGenerateNewKey(ALIAS_1, AES, CBC, PKCS7)
        assertTrue(generated == saved)
        assertFalse(another == saved)
    }

    @Test
    fun testDeleteKeyWhenSameTransformation() {
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES, CBC, PKCS7)
        assertTrue(keyContainer.hasKey(ALIAS_1, AES, CBC, PKCS7))

        keyContainer.deleteKey(ALIAS_1, AES, CBC, PKCS7)

        assertFalse(keyContainer.hasKey(ALIAS_1, AES, CBC, PKCS7))
    }

    @Test
    fun testDeleteKeyWhenDifferentTransformation() {
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES, CBC, PKCS7)
        keyContainer.getOrGenerateNewKey(ALIAS_2, AES, CBC, PKCS7)

        keyContainer.deleteKey(ALIAS_1, AES, CBC, PKCS7)

        assertFalse(keyContainer.hasKey(ALIAS_1, AES, CBC, PKCS7))
        assertTrue(keyContainer.hasKey(ALIAS_2, AES, CBC, PKCS7))
    }

    @Test
    fun testDeleteKeyWithoutEntry() {
        keyContainer.deleteKey(ALIAS_1, AES, CBC, PKCS7)
        assertFalse(keyContainer.hasKey(ALIAS_1, AES, CBC, PKCS7))
    }

    @Test
    fun testHasKey() {
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES, CBC, PKCS7)
        assertTrue(keyContainer.hasKey(ALIAS_1, AES, CBC, PKCS7))
        assertFalse(keyContainer.hasKey(ALIAS_2, AES, CBC, PKCS7))
    }

    companion object {

        private const val ALIAS_1 = "alias_1"

        private const val ALIAS_2 = "alias_2"

    }


}
