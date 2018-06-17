package jp.ishikota.cryptopreference.keycontainer

import jp.ishikota.cryptopreference.keycontainer.androidkeystore.AndroidKeyStoreContainer
import jp.ishikota.cryptopreference.keycontainer.compat.SecretKeyContainerCompat

internal class SecretKeyContainerFactory(private val keySecretForCompat: ByteArray?) {

    fun create(): SecretKeyContainer =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AndroidKeyStoreContainer()
        } else {
            if (keySecretForCompat == null) {
                throw RuntimeException("Try to use compat key container but secret is not passed.")
            }
            SecretKeyContainerCompat(keySecretForCompat)
        }

}
