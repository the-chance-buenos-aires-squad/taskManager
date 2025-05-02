package data.repositories

import java.security.MessageDigest

class MD5Hasher {
    fun hash(input: String): String {

        val messageDigest = MessageDigest.getInstance("MD5")

        return messageDigest.digest(input.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }
}