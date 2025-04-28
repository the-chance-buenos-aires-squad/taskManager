package dummyData

import org.buinos.domain.entities.User
import org.buinos.domain.entities.UserRole
import java.time.LocalDateTime
import java.util.*

fun createDummyUser(
    id: String,
    username: String,
    password: String,
    role: UserRole,
    createdAt: LocalDateTime = LocalDateTime.now(),
): User {
    return User(
        id = id,
        username = username,
        password = password,
        role = role,
        createdAt = createdAt
    )
}