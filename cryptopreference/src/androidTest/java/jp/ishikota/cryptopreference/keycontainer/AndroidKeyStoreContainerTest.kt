package jp.ishikota.cryptopreference.keycontainer

import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidKeyStoreContainerTest {

    private lateinit var keyContainer: AndroidKeyStoreContainer

    @Before
    fun setup() {
        keyContainer = AndroidKeyStoreContainer()
    }

    @After
    fun tearUp() {
        keyContainer.deleteKey(ALIAS_1, AES)
        keyContainer.deleteKey(ALIAS_1, RSA)
        keyContainer.deleteKey(ALIAS_2, AES)
        keyContainer.deleteKey(ALIAS_2, RSA)
    }

    @Test
    fun testGenerateNewKey() {
        assertFalse(keyContainer.hasKey(ALIAS_1, AES))
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES)
        assertTrue(keyContainer.hasKey(ALIAS_1, AES))
    }

    @Test
    fun testGetSavedKey() {
        val generated = keyContainer.getOrGenerateNewKey(ALIAS_1, AES)
        val saved = keyContainer.getOrGenerateNewKey(ALIAS_1, AES)
        assertEquals(generated, saved)
    }

    @Test
    fun testDeleteKeyWhenSignatureIsSame() {
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES)
        assertTrue(keyContainer.hasKey(ALIAS_1, AES))

        keyContainer.deleteKey(ALIAS_1, AES)

        assertFalse(keyContainer.hasKey(ALIAS_1, AES))
    }

    @Test
    fun testDeleteKeyWhenSameAliasButDifferentAlgorithm() {
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES)
        keyContainer.getOrGenerateNewKey(ALIAS_1, RSA)

        keyContainer.deleteKey(ALIAS_1, RSA)

        assertTrue(keyContainer.hasKey(ALIAS_1, AES))
        assertFalse(keyContainer.hasKey(ALIAS_1, RSA))
    }

    @Test
    fun testDeleteLeyWhenDifferentAliasButSameAlgorithm() {
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES)
        keyContainer.getOrGenerateNewKey(ALIAS_2, AES)

        keyContainer.deleteKey(ALIAS_1, AES)

        assertFalse(keyContainer.hasKey(ALIAS_1, AES))
        assertTrue(keyContainer.hasKey(ALIAS_2, AES))
    }

    @Test
    fun testDeleteKeyWithoutEntry() {
        keyContainer.deleteKey(ALIAS_1, AES)
        assertFalse(keyContainer.hasKey(ALIAS_1, AES))
    }

    @Test
    fun testHasKey() {
        keyContainer.getOrGenerateNewKey(ALIAS_1, AES)
        assertTrue(keyContainer.hasKey(ALIAS_1, AES))
        assertFalse(keyContainer.hasKey(ALIAS_1, RSA))
        assertFalse(keyContainer.hasKey(ALIAS_2, AES))
    }

    companion object {

        private const val ALIAS_1 = "alias_1"

        private const val ALIAS_2 = "alias_2"
        
        private const val AES = "AES"
        
        private const val RSA = "RSA"

    }


}
