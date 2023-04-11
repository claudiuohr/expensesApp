package com.sd.laborator.services

import com.sd.laborator.interfaces.EncryptionInterface
import org.springframework.stereotype.Service
import java.security.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.*


@Service
class EncryptionService : EncryptionInterface {

    private val secretKey: SecretKey
    private val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    private val iv: IvParameterSpec

    init {
        val password = "MySecretPassword"

        // Generate a 256-bit key using a key derivation function
        val salt = "SomeRandomSalt".toByteArray()
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

        // Use a fixed 16-byte IV
        val ivBytes = "MyFixedIV1234567".toByteArray()
        iv = IvParameterSpec(ivBytes)
    }

    override fun encrypt(input: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
        val encrypted = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    override fun decrypt(input: String): String {
        val encrypted = Base64.getDecoder().decode(input)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }

    override fun hash(input: String): String {
        // Use SHA-256 to hash the username and password
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
}
