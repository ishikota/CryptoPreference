package jp.ishikota.cryptopreference.keycontainer

import javax.crypto.SecretKey

interface SecretKeyContainer {

    fun getKey(alias: String): SecretKey

    fun deleteKey(alias: String)

    fun getAliases(): List<String>

}
