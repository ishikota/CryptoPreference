package jp.ishikota.cryptopreference.preference.plain

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlainPreferenceTest {

    private lateinit var preference: PlainPreference

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getTargetContext()
        preference = PlainPreferenceFactory("CryptoPreference").create(appContext, debugMode = false)
    }

    @After
    fun tearUp() {
        preference.clear()
    }

    @Test
    fun testSaveGetString() {
        val txt = "some text to save"
        assertEquals("", preference.getString(KEY_STRING))

        preference.saveString(KEY_STRING, txt)
        assertEquals(txt, preference.getString(KEY_STRING))
    }

    @Test
    fun testDeleteString() {
        val txt = "some text to save"
        preference.saveString(KEY_STRING, txt)
        preference.deleteString(KEY_STRING)
        assertEquals("", preference.getString(KEY_STRING))
    }

    @Test
    fun testHasStringKey() {
        Assert.assertFalse(preference.hasStringKey(KEY_STRING))
        preference.saveString(KEY_STRING, "hoge")
        assertTrue(preference.hasStringKey(KEY_STRING))
    }

    @Test
    fun testSaveGetIv() {
        val iv = "0123456789123456".toByteArray()
        assertTrue(preference.getIv(KEY_IV).isEmpty())

        preference.saveIv(KEY_IV, iv)
        assertTrue(compareByteArray(iv, preference.getIv(KEY_IV)))
    }

    @Test
    fun testDeleteIv() {
        val iv = "0123456789123456".toByteArray()
        preference.saveIv(KEY_IV, iv)
        preference.deleteIv(KEY_IV)
        assertTrue(compareByteArray("".toByteArray(), preference.getIv(KEY_IV)))
    }

    @Test
    fun testHasIvKey() {
        val iv = "0123456789123456".toByteArray()
        Assert.assertFalse(preference.hasIvKey(KEY_IV))
        preference.saveIv(KEY_IV, iv)
        assertTrue(preference.hasIvKey(KEY_IV))
    }

    @Test
    fun testKeyObfuscation() {
        preference.saveString(KEY_STRING, "hoge")
        preference.saveIv(KEY_IV, "0123456789123456".toByteArray())

        val rawPreference = InstrumentationRegistry.getTargetContext()
            .getSharedPreferences("CryptoPreference", Context.MODE_PRIVATE)
        assertFalse(rawPreference.contains(KEY_STRING))
        assertFalse(rawPreference.contains(KEY_IV))
    }

    @Test
    fun testClear() {
        preference.saveString(KEY_STRING, "hoge")
        preference.saveString(KEY_TEST, "fuga")
        preference.clear()

        assertEquals("", preference.getString(KEY_STRING))
        assertEquals("", preference.getString(KEY_TEST))
    }

    private fun compareByteArray(expected: ByteArray, target: ByteArray): Boolean = expected.contentEquals(target)

    companion object {

        private const val KEY_STRING = "KEY_STRING"

        private const val KEY_TEST = "KEY_TEST"

        private const val KEY_IV = "KEY_IV"

    }
}
