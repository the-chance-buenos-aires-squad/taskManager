package domain.util

import java.security.MessageDigest

object MD5Hash {
    fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
