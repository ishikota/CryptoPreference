package jp.ishikota.cryptopreference.keyfactory.aes.compat

import org.junit.Test
import java.security.InvalidKeyException

class AesSecretKeyFactoryTest {

    @Test
    fun supportedSizeOfKeyIsPassed() {
        AESSecretKeyFactory("1234567890123456".toByteArray())
    }

    @Test(expected = InvalidKeyException::class)
    fun notSupportedSizeOfKeyIsPassed() {
        AESSecretKeyFactory("12345678".toByteArray())
    }

}
