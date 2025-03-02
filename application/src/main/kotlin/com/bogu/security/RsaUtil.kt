package com.bogu.security

import jakarta.annotation.PostConstruct
import mu.KLogging
import org.bouncycastle.util.io.pem.PemReader
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


@Component
class RsaUtil(private val resourceLoader: ResourceLoader) {
    private lateinit var privateKey: PrivateKey
    private lateinit var publicKey: PublicKey

    @PostConstruct
    fun init() {
        this.privateKey = loadPrivateKey()
        this.publicKey = loadPublicKey()
    }

    private fun loadPrivateKey(): PrivateKey {
        val resource = resourceLoader.getResource("classpath:$PRIVATE_KEY_FILE_NAME")
        val reader = PemReader(InputStreamReader(resource.inputStream))
        val pemObject = reader.readPemObject()
        reader.close()

        val keySpec = PKCS8EncodedKeySpec(pemObject.content)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec)
    }

    private fun loadPublicKey(): PublicKey {
        val resource = resourceLoader.getResource("classpath:$PUBLIC_KEY_FILE_NAME")
        val reader = PemReader(InputStreamReader(resource.inputStream))
        val pemObject = reader.readPemObject()
        reader.close()

        val keySpec = X509EncodedKeySpec(pemObject.content)
        return KeyFactory.getInstance("RSA").generatePublic(keySpec)
    }

    fun getPrivateKey(): PrivateKey = privateKey

    fun decrypt(encryptedText: String): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey())
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }

    fun getPublicKeyAsString(): String {
        val resource = resourceLoader.getResource("classpath:$PUBLIC_KEY_FILE_NAME")
        return resource.inputStream.bufferedReader().use { it.readText() }
    }

    companion object : KLogging() {
        const val PRIVATE_KEY_FILE_NAME = "private_key.pem"
        const val PUBLIC_KEY_FILE_NAME = "public_key.pem"
    }
}