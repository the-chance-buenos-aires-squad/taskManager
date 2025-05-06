package data.repositories.mappers.bson

import com.google.common.truth.Truth.assertThat
import domain.entities.User
import domain.entities.UserRole
import org.bson.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class UserBsonMapperTest {
    private val mapper = UserBsonMapper()

    @Test
    fun `toDocument should map User to Document`() {
        val now = LocalDateTime.now()
        val user = User(UUID.randomUUID(), "ahmed", "pass", UserRole.ADMIN, now)

        val doc = mapper.toDocument(user)

        assertThat(doc.getString("username")).isEqualTo(user.username)
    }

    @Test
    fun `fromDocument should map Document to User`() {
        val now = LocalDateTime.now()
        val doc = Document()
            .append("id", UUID.randomUUID().toString())
            .append("username", "ahmed")
            .append("password", "pass")
            .append("role", "ADMIN")
            .append("createdAt", now.toString())

        val user = mapper.fromDocument(doc)

        assertThat(user.username).isEqualTo("ahmed")
    }
}