package com.sd.laborator.interfaces

interface EncryptionInterface {
    fun encrypt(input: String): String
    fun decrypt(input: String): String
    fun hash(input: String): String
}