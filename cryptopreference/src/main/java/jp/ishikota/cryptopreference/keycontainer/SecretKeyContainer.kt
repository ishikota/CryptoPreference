package jp.ishikota.cryptopreference.keycontainer

import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keyfactory.SecretKeyFactory
import java.security.Key

internal interface SecretKeyContainer {

    fun getOrGenerateNewKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): Key

    fun deleteKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    )

    fun hasKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): Boolean

    class TransformationNotSupportedException(
        algorithm: Algorithm, blockMode: BlockMode, padding: Padding
    ): RuntimeException(
        "The transformation ${algorithm.label}/${blockMode.label}/${padding.label} is not supported."
    )

    class TooManySecretKeyFactoryException(
        factories: List<SecretKeyFactory>, algorithm: Algorithm, blockMode: BlockMode, padding: Padding
    ): RuntimeException(
        "More than one SecretKeyFactory found for a transformation. factories=$factories, transformation=${algorithm.label}/${blockMode.label}/${padding.label}"
    )

}
