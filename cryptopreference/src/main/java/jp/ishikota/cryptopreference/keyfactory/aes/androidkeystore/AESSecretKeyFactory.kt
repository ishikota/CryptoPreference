package jp.ishikota.cryptopreference.keyfactory.aes.androidkeystore

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keyfactory.SecretKeyFactory
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
internal class AESSecretKeyFactory: SecretKeyFactory {

    override fun isSupported(
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ) = algorithm == Algorithm.AES &&
        blockMode == BlockMode.CBC &&
        padding == Padding.PKCS7

    override fun create(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM, ANDROID_KEY_STORE)
        keyGenerator.init(KeyGenParameterSpec.Builder(alias, KEY_PURPOSE)
            .setBlockModes(blockModeToStr(blockMode))
            .setEncryptionPaddings(paddingToStr(padding))
            .build())
        return keyGenerator.generateKey()
    }

    private fun blockModeToStr(blockMode: BlockMode) = when(blockMode) {
        BlockMode.CBC -> KeyProperties.BLOCK_MODE_CBC
    }

    private fun paddingToStr(padding: Padding) = when(padding) {
        Padding.PKCS7 -> KeyProperties.ENCRYPTION_PADDING_PKCS7
    }

    companion object {

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

        private const val KEY_PURPOSE = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT

    }


}
