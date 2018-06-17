package jp.ishikota.cryptopreference.keyfactory

import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import javax.crypto.SecretKey

internal interface SecretKeyFactory {

    fun isSupported(
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): Boolean

    fun create(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): SecretKey

}
