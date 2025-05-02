package data.repositories

import domain.entities.User
import domain.repositories.AuthRepository

class AuthRepositoryImpl : AuthRepository {

    private var currentUser: User? = null

    override fun login(user: User) {
        currentUser = user
    }

    override fun logout() {
        currentUser = null
    }

    override fun getCurrentUser(): User? = currentUser

}