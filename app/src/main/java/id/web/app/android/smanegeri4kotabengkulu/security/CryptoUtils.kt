package id.web.app.android.smanegeri4kotabengkulu.security

import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Cryptographic utilities matching the shipped APK AES decryption and SHA-256 hex formatting.
 */
object CryptoUtils {

    private const val AES_ALGORITHM = "AES"
    private const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val AES_KEY = "4H9d2J7kZ1qW8sL0"

    /**
     * Decrypts a string formatted as "<base64Iv>:<base64Ciphertext>" using AES-128-CBC/PKCS5Padding.
     */
    fun decrypt(encryptedPayload: String): String {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val keyBytes = AES_KEY.toByteArray(Charsets.UTF_8)
        val secretKey = SecretKeySpec(keyBytes, AES_ALGORITHM)
        val parts = encryptedPayload.split(":")
        val ivBytes = Base64.getDecoder().decode(parts[0])
        val cipherBytes = Base64.getDecoder().decode(parts[1])
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(ivBytes))
        val plainBytes = cipher.doFinal(cipherBytes)
        return String(plainBytes, Charsets.UTF_8)
    }

    /**
     * Computes a lowercase hexadecimal SHA-256 digest without byte separators.
     */
    fun sha256Hex(input: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(input)
        return digest.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
