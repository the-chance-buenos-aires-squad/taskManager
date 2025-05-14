package data.dataSource.util.hash

interface PasswordHash {
    fun generateHash(password: String): String
}