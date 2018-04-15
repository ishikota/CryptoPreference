package jp.ishikota.cryptopreference.keycontainer.androidkeystore

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.keycontainer.SecretKeyFactory
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
internal class AESSecretKeyFactory: SecretKeyFactory {

    override fun isSupported(
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ) = algorithm == SecretKeyContainer.Algorithm.AES &&
        blockMode == SecretKeyContainer.BlockMode.CBC &&
        padding == SecretKeyContainer.Padding.PKCS7

    override fun create(
        alias: String,
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM, ANDROID_KEY_STORE)
        keyGenerator.init(KeyGenParameterSpec.Builder(alias, KEY_PURPOSE)
            .setBlockModes(blockModeToStr(blockMode))
            .setEncryptionPaddings(paddingToStr(padding))
            .build())
        return keyGenerator.generateKey()
    }

    private fun blockModeToStr(blockMode: SecretKeyContainer.BlockMode) = when(blockMode) {
        SecretKeyContainer.BlockMode.CBC -> KeyProperties.BLOCK_MODE_CBC
    }

    private fun paddingToStr(padding: SecretKeyContainer.Padding) = when(padding) {
        SecretKeyContainer.Padding.PKCS7 -> KeyProperties.ENCRYPTION_PADDING_PKCS7
    }

    companion object {

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

        private const val KEY_PURPOSE = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT

    }


}
