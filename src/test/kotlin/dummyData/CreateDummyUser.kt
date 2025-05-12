package dummyData

import domain.entities.User
import domain.entities.UserRole
import java.time.LocalDateTime
import java.util.*

fun createDummyUser(
    id: UUID,
    username: String,
    role: UserRole,
    createdAt: LocalDateTime = LocalDateTime.now(),
): User {
    return User(
        id = id,
        username = username,
        role = role,
        createdAt = createdAt
    )
}