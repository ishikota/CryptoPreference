package jp.ishikota.cryptopreference.keycontainer

import java.security.Key

interface SecretKeyContainer {

    fun getKey(alias: String): Key

    fun deleteKey(alias: String)

    fun getAliases(): List<String>

}
