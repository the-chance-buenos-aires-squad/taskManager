package data.repositories.mappers.bson

import com.google.common.truth.Truth.assertThat
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import org.bson.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AuditBsonMapperTest {
    private val mapper = AuditBsonMapper()

    @Test
    fun `toDocument should map Audit to Document`() {
        val audit = Audit(
            id = "1",
            entityId = "2",
            entityType = EntityType.PROJECT,
            action = ActionType.UPDATE,
            field = "name",
            oldValue = "Old Name",
            newValue = "New Name",
            userId = "user1",
            timestamp = LocalDateTime.now()
        )

        val document = mapper.toDocument(audit)

        assertThat(document.getString("id")).isEqualTo(audit.id)
    }

    @Test
    fun `fromDocument should map Document to Audit`() {
        val now = LocalDateTime.now()
        val doc = Document()
            .append("id", "1")
            .append("entityId", "2")
            .append("entityType", "PROJECT")
            .append("action", "UPDATE")
            .append("field", "name")
            .append("oldValue", "Old Name")
            .append("newValue", "New Name")
            .append("userId", "user1")
            .append("timestamp", now.toString())

        val audit = mapper.fromDocument(doc)

        assertThat(audit.entityType).isEqualTo(EntityType.PROJECT)
    }
}