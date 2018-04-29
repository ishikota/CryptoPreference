package jp.ishikota.cryptopreference

enum class Algorithm(val label: String) {
    AES("AES")
}

enum class BlockMode(val label: String) {
    CBC("CBC")
}

enum class Padding(val label: String) {
    PKCS7("PKCS7Padding")
}
