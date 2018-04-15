package jp.ishikota.cryptopreference.keycontainer

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

    enum class Algorithm(val label: String) {
        AES("AES")
    }

    enum class BlockMode(val label: String) {
        CBC("CBC")
    }

    enum class Padding(val label: String) {
        PKCS7("PKCS7Padding")
    }

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
