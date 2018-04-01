package jp.ishikota.cryptopreference.keycontainer

import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert
import junit.framework.Assert.assertEquals
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
        keyContainer.getAliases().forEach {
            keyContainer.deleteKey(it)
        }
    }

    @Test
    fun testGetKeyAndGenerateNewKey() {
        assertEquals(0, keyContainer.getAliases().size)
        keyContainer.getKey(ALIAS_1)
        assertEquals(1, keyContainer.getAliases().size)

    }

    @Test
    fun testGetKeyAndFetchGeneratedKey() {
        val alias1Generated = keyContainer.getKey(ALIAS_1)
        assertEquals(1, keyContainer.getAliases().size)

        val alias1Fetched = keyContainer.getKey(ALIAS_1)
        assertEquals(alias1Generated, alias1Fetched)

        val alias2Generated = keyContainer.getKey(ALIAS_2)
        assertEquals(2, keyContainer.getAliases().size)

        val alias2Fetched = keyContainer.getKey(ALIAS_2)
        assertEquals(alias2Generated, alias2Fetched)
        Assert.assertFalse(alias1Generated == alias2Fetched)
    }

    @Test
    fun testDeleteKey() {
        keyContainer.getKey(ALIAS_1)
        assertEquals(1, keyContainer.getAliases().size)

        keyContainer.deleteKey(ALIAS_1)
        assertEquals(0, keyContainer.getAliases().size)
    }

    @Test
    fun testDeleteKeyWithoutEntry() {
        keyContainer.deleteKey(ALIAS_1)
        assertEquals(0, keyContainer.getAliases().size)
    }

    @Test
    fun testGetAliases() {
        keyContainer.getKey(ALIAS_2)
        keyContainer.getKey(ALIAS_2)
        keyContainer.getKey(ALIAS_1)
        assertEquals(listOf(ALIAS_2, ALIAS_1), keyContainer.getAliases())
    }

    companion object {

        private const val ALIAS_1 = "alias_1"

        private const val ALIAS_2 = "alias_2"

    }


}
