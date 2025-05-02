package data.repositories

import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import java.time.LocalDateTime
import java.util.UUID

class AuthRepositoryImpl(
    private val userRepository: UserRepository, private val mD5Hasher: PasswordHasher
) : AuthRepository {

    private var currentUser: User? = null

    override fun login(username: String, password: String): User? {

        val user = userRepository.getUserByUserName(username.trim()) ?: return null

        val hashedInput = mD5Hasher.hash(password)
        if (user.password != hashedInput) {
            return null
        }

        currentUser = user
        return user;
    }

    override fun addUser(userName: String, password: String): User? {

        if (getCurrentUser() == null || getCurrentUser()!!.role == UserRole.MATE) return null

        val user = userRepository.getUserByUserName(userName.trim())
        if (user != null) return null

        val newUserHashedPassword = mD5Hasher.hash(password)

        val newUser = User(
            id = UUID.randomUUID(),
            username = userName.trim(),
            password = newUserHashedPassword,
            role = UserRole.MATE,
            createdAt = LocalDateTime.now()
        )
        userRepository.addUser(newUser)
        return newUser
    }


    override fun logout() {
        currentUser = null
    }

    override fun getCurrentUser(): User? = currentUser

}