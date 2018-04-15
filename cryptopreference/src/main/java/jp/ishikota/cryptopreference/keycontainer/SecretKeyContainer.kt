package jp.ishikota.cryptopreference.keycontainer

import java.security.Key

interface SecretKeyContainer {

    fun getOrGenerateNewKey(alias: String, algorithm: String): Key

    fun deleteKey(alias: String, algorithm: String)

    fun hasKey(alias: String, algorithm: String): Boolean

}
