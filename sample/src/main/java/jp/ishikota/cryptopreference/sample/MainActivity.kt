package jp.ishikota.cryptopreference.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.ishikota.cryptopreference.MultiModuleTest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MultiModuleTest.test()
    }
}
