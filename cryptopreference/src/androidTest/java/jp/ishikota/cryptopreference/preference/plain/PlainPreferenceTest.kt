package jp.ishikota.cryptopreference.preference.plain

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
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
        preference = PlainPreferenceImpl(appContext)
    }

    @After
    fun tearUp() {
        preference.delete(KEY_STRING)
        preference.delete(KEY_TEST)
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
        preference.delete(KEY_STRING)
        assertEquals("", preference.getString(KEY_STRING))
    }

    @Test
    fun testHasKey() {
        assertFalse(preference.hasKey(KEY_STRING))
        preference.saveString(KEY_STRING, "hoge")
        assertTrue(preference.hasKey(KEY_STRING))
    }

    @Test
    fun testClear() {
        preference.saveString(KEY_STRING, "hoge")
        preference.saveString(KEY_TEST, "fuga")
        preference.clear()

        assertEquals("", preference.getString(KEY_STRING))
        assertEquals("", preference.getString(KEY_TEST))
    }

    companion object {

        private const val KEY_STRING = "KEY_STRING"

        private const val KEY_TEST = "KEY_TEST"

    }
}
