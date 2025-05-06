package data.repositories.mappers.bson

import domain.entities.User
import domain.entities.UserRole
import org.bson.Document
import java.time.LocalDateTime
import java.util.*

class UserBsonMapper:BsonMapper<User> {
    override fun toDocument(entity: User): Document {
        return Document()
            .append("id", entity.id.toString())
            .append("username", entity.username)
            .append("password", entity.password)
            .append("role", entity.role.name)
            .append("createdAt", entity.createdAt.toString())
    }

    override fun fromDocument(document: Document): User {
        return User(
            id = UUID.fromString(document.getString("id")),
            username = document.getString("username"),
            password = document.getString("password"),
            role = UserRole.valueOf(document.getString("role")),
            createdAt = LocalDateTime.parse(document.getString("createdAt"))
        )
    }
}