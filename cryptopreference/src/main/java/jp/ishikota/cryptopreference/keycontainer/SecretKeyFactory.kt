package jp.ishikota.cryptopreference.keycontainer

import javax.crypto.SecretKey

internal interface SecretKeyFactory {

    fun isSupported(
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ): Boolean

    fun create(
        alias: String,
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ): SecretKey

}
