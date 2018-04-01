package jp.ishikota.cryptopreference.cipher

open class CipherLogicException : RuntimeException {

    constructor(detailMessage: String) : super(detailMessage)

    constructor(detailMessage: String, throwable: Throwable) : super(detailMessage, throwable)

    constructor(throwable: Throwable) : super(throwable)
}

