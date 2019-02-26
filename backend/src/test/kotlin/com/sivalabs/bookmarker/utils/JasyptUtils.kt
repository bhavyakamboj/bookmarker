package com.sivalabs.bookmarker.utils

import org.jasypt.util.text.BasicTextEncryptor

fun main() {
    val encryptor = BasicTextEncryptor()
    encryptor.setPassword("dummy")

    listOf("secret")
        .forEach {
            println("plain text: '$it', encrypted: '${encryptor.encrypt(it)}'")
        }
}
