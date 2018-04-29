package jp.ishikota.cryptopreference.preference.encrypted

import jp.ishikota.cryptopreference.cryptor.CipherCryptor
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.preference.bytearrayencoder.Base64Encoder
import jp.ishikota.cryptopreference.preference.bytearrayencoder.ByteArrayEncoder
import jp.ishikota.cryptopreference.preference.plain.PlainPreference

internal class EncryptedPreferenceFactory {

    fun create(
        secretKeyContainer: SecretKeyContainer,
        plainPreference: PlainPreference,
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding,
        byteArrayEncoder: ByteArrayEncoder = Base64Encoder()
    ): EncryptedPreference {
        val cryptor = initializeCryptor(secretKeyContainer, plainPreference, algorithm, blockMode, padding)
        return EncryptedPreferenceImpl(cryptor, plainPreference, byteArrayEncoder)
    }

    private fun initializeCryptor(
        secretKeyContainer: SecretKeyContainer,
        plainPreference: PlainPreference,
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ) = CipherCryptor(
        secretKeyContainer,
        plainPreference,
        algorithm,
        blockMode,
        padding
    )

}
