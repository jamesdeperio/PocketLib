package jdp.pocketlib.util

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec



class AESUtil(private var keyValue:String="secure_key") {
     private var cipher: Cipher? = null
    private var secretKey: SecretKey? = null
    private var keyFactory: SecretKeyFactory? = null
    private var pbeKey: PBEKeySpec? = null
    private var paramSpec: PBEParameterSpec? = null
    private var salt = byteArrayOf(111, 123, 56, 123, 99, 108, 45, 65)
    private val iterationCount = 21
    private var hex=" 0123456789ABCDEF"

    init {
        this.paramSpec = PBEParameterSpec(this.salt, this.iterationCount)
        this.pbeKey = PBEKeySpec(this.keyValue.toCharArray())
        this.keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
        this.secretKey = keyFactory!!.generateSecret(this.pbeKey)
        this.cipher = Cipher.getInstance("PBEWithMD5AndDES")
    }

    @Throws(Exception::class)
    fun encrypt(cleartext: ByteArray): String {
        cipher!!.init(Cipher.ENCRYPT_MODE, this.secretKey, this.paramSpec)
        val data:String = Arrays.toString(cipher!!.doFinal(cleartext))
        return data
    }

    @Throws(Exception::class)
    fun decrypt(encrypted: String): String {
        cipher!!.init(Cipher.DECRYPT_MODE, this.secretKey, this.paramSpec)
        val split = encrypted.substring(1, encrypted.length - 1).split(", ")
        val array = ByteArray(split.size)
        for (i in split.indices) array[i] = java.lang.Byte.parseByte(split[i])
       val decryptedText = cipher!!.doFinal(array)
        return  String(decryptedText)
    }
}