package data.dataSource.util.hash

import java.security.MessageDigest

class MD5PasswordHash(private val messageDigest: MessageDigest) : PasswordHash {

    override fun generateHash(password: String): String {
        return messageDigest.digest(password.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }
}