package jp.ishikota.cryptopreference.preference.plain

import android.content.Context

internal class PlainPreferenceFactory {

    fun create(context: Context, debugMode: Boolean): PlainPreference =
        PlainPreferenceImpl(context, PREF_NAME, debugMode)

    companion object {

        private const val PREF_NAME = "CryptoPreference"

    }

}
