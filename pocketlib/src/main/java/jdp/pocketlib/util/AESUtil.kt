package jdp.pocketlib.util

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
    fun encrypt(cleartext: String): String {
        cipher!!.init(Cipher.ENCRYPT_MODE, this.secretKey, this.paramSpec)
        val encrypted = cipher!!.doFinal(cleartext.toByteArray())
        return toHex(encrypted)
    }

    @Throws(Exception::class)
    fun decrypt(encrypted: String): String {
        cipher!!.init(Cipher.DECRYPT_MODE, this.secretKey, this.paramSpec)
       val decryptedText = cipher!!.doFinal(toByte(encrypted))

        return  String(decryptedText)
    }

    private fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len)
            result[i] = Integer.valueOf(
                hexString.substring(2 * i, 2 * i + 2),
                16
            ).toByte()
        return result
    }

    private fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) appendHex(result, buf[i])
        return result.toString()
    }

    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(hex[(b.toInt() shr 4) and 0x0f])
        sb.append(hex[b.toInt()  and 0x0f])
    }
}