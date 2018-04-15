package jp.ishikota.cryptopreference.sample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val key = "abcdefghijklmnop"
        val encryptedPreference1 = EncryptedPreference1(key, PlainPreference(this, debugMode = true))

        button_save.setOnClickListener {
            val k = edit_key.text.toString()
            val v = edit_value.text.toString()
            if (k.isEmpty() || v.isEmpty()) {
                Toast.makeText(this, "enter info!!", Toast.LENGTH_SHORT).show()
            } else {
                encryptedPreference1.savePrivateString(k, v)
                edit_key.setText("")
                edit_value.setText("")
            }
        }

        button_get.setOnClickListener {
            val k = edit_key.text.toString()
            if (k.isEmpty()) {
                Toast.makeText(this, "enter info!!", Toast.LENGTH_SHORT).show()
            } else {
                val v = encryptedPreference1.getPrivateString(k)
                edit_value.setText(v)
            }
        }
    }
}

class EncryptedPreference1(
    private val key: String,
    private val plainPreference: PlainPreference) {

    fun savePrivateString(alias: String, value: String) {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
            val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
            init(Cipher.ENCRYPT_MODE, secretKeySpec)
            saveIv(alias, iv)
        }
        val encrypted = tryDoFinal(cipher, value.toByteArray())
        plainPreference.saveString(obfuscate(alias), Base64.encodeToString(encrypted, Base64.DEFAULT))
    }

    fun getPrivateString(alias: String): String {
        val encrypted = plainPreference.getString(obfuscate(alias))
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
            val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
            init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(getIv(alias)))
        }
        val decrypted = tryDoFinal(cipher, Base64.decode(encrypted, Base64.DEFAULT))
        return String(decrypted)
    }

    private fun tryDoFinal(cipher: Cipher, byteArray: ByteArray): ByteArray =
        try {
            val result = cipher.doFinal(byteArray)
            if (result == null) {
                Log.e("tryDoFinal", "cipher.doFinal returns null. byteArray=${byteArray.toList()}")
                byteArrayOf()
            } else {
                result
            }
        } catch (e: Exception) {
            Log.e("tryDoFinal", e.message)
            when (e) {
                is IllegalBlockSizeException,
                is BadPaddingException -> {
                    byteArrayOf()
                }
                else -> throw e
            }
        }

    private fun initializeCipher(mode: Int) = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
        val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(fetchSavedIV().toByteArray())
        init(mode, secretKeySpec, ivSpec)
    }

    private fun fetchSavedIV(): String {
        if (!plainPreference.hasKey("EXTRA_IV")) {
            plainPreference.saveString("EXTRA_IV", generateIV())
        }
        return plainPreference.getString("EXTRA_IV")
    }

    private fun generateIV(): String {
        return UUID.randomUUID().toString().substring(0, 16) // IV length must be 16 bytes
    }

    private fun getIVKey(alias: String) = "${PREF_KEY_IV_PREFIX}_$alias"

    private fun saveIv(alias: String, iv: ByteArray) = plainPreference.saveString(getIVKey(alias), encodeToBase64(iv))

    private fun getIv(alias: String) = decodeFromBase64(plainPreference.getString(getIVKey(alias)))

    private fun encodeToBase64(byteArray: ByteArray) = Base64.encodeToString(byteArray, Base64.DEFAULT)

    private fun decodeFromBase64(encoded: String) = Base64.decode(encoded, Base64.DEFAULT)

    private fun obfuscate(key: String) = HashUtils.generateSha256(key)

    companion object {

        private const val PREF_KEY_IV_PREFIX = "pref_key_iv_prefix"

    }

}


class PlainPreference(private val context: Context, private val debugMode: Boolean = false) {

    fun saveString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
        if (debugMode) {
            Log.d(TAG, "saved string [ $value ] with key [ $key ]")
        }
    }

    fun getString(key: String): String = pref.getString(key, DEFAULT_STRING)

    fun delete(key: String) {
        pref.edit().remove(key).apply()
        if (debugMode) {
            Log.d(TAG, "deleted string which saved with key [ $key ]")
        }
    }

    fun hasKey(key: String): Boolean = pref.contains(key)

    fun clear() {
        pref.all.keys.forEach { delete(it) }
    }

    private val pref: SharedPreferences
        get() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {

        private const val TAG = "PlainPreferenceImpl"

        private const val PREF_NAME = "PlainPreference"

        private const val DEFAULT_STRING = ""

    }
}

class HashUtils {

    companion object {

        fun generateSha256(source: String) = generateHash(source, ALGORITHM_SHA_256)

        private const val ALGORITHM_SHA_256 = "SHA-256"

        private fun generateHash(source: String, algorithm: String): String = try {
            val digest = MessageDigest.getInstance(algorithm)
            digest.update(source.toByteArray())
            bytesToHex(digest.digest())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("HashUtils", e.message, e)
            ""
        }

        private fun bytesToHex(bytes: ByteArray) = bytes.map { String.format("%02x", it) }.joinToString("")
    }
}
