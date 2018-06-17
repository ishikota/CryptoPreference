package jp.ishikota.cryptopreference.preference.encrypted

import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.cryptor.CipherCryptor
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.bytearrayencoder.Base64Encoder
import jp.ishikota.cryptopreference.bytearrayencoder.ByteArrayEncoder
import jp.ishikota.cryptopreference.preference.plain.PlainPreference

internal class EncryptedPreferenceFactory {

    fun create(
        secretKeyContainer: SecretKeyContainer,
        plainPreference: PlainPreference,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding,
        byteArrayEncoder: ByteArrayEncoder = Base64Encoder()
    ): EncryptedPreference {
        val cryptor = initializeCryptor(secretKeyContainer, plainPreference, algorithm, blockMode, padding)
        return EncryptedPreferenceImpl(cryptor, plainPreference, byteArrayEncoder)
    }

    private fun initializeCryptor(
        secretKeyContainer: SecretKeyContainer,
        plainPreference: PlainPreference,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ) = CipherCryptor(
        secretKeyContainer,
        plainPreference,
        algorithm,
        blockMode,
        padding
    )

}
