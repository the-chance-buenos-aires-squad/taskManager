package data.repositories.mappers.bson

import com.google.common.truth.Truth.assertThat
import domain.entities.Task
import org.bson.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class TaskBsonMapperTest {
    private val mapper = TaskBsonMapper()

    @Test
    fun `toDocument should map Task to Document`() {
        val now = LocalDateTime.now()
        val task = Task(
            id = UUID.randomUUID(),
            title = "Task",
            description = "Task desc",
            projectId = UUID.randomUUID(),
            stateId = UUID.randomUUID(),
            assignedTo = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = now,
            updatedAt = now
        )

        val doc = mapper.toDocument(task)

        assertThat(doc.getString("title")).isEqualTo(task.title)
    }

    @Test
    fun `toDocument should map Task to Document when assignedTo is null`() {
        val now = LocalDateTime.now()
        val task = Task(
            id = UUID.randomUUID(),
            title = "Task",
            description = "Task desc",
            projectId = UUID.randomUUID(),
            stateId = UUID.randomUUID(),
            assignedTo = null,
            createdBy = UUID.randomUUID(),
            createdAt = now,
            updatedAt = now
        )

        val doc = mapper.toDocument(task)

        assertThat(doc.getString("title")).isEqualTo(task.title)
    }

    @Test
    fun `fromDocument should map Document to Task when assignedTo is null`() {
        val now = LocalDateTime.now()
        val doc = Document()
            .append("id", UUID.randomUUID().toString())
            .append("title", "Task")
            .append("description", "Task desc")
            .append("projectId", UUID.randomUUID().toString())
            .append("stateId", UUID.randomUUID().toString())
            .append("assignedTo", null)
            .append("createdBy", UUID.randomUUID().toString())
            .append("createdAt", now.toString())
            .append("updatedAt", now.toString())

        val task = mapper.fromDocument(doc)

        assertThat(task.title).isEqualTo("Task")
    }

    @Test
    fun `fromDocument should map Document to Task`() {
        val now = LocalDateTime.now()
        val doc = Document()
            .append("id", UUID.randomUUID().toString())
            .append("title", "Task")
            .append("description", "Task desc")
            .append("projectId", UUID.randomUUID().toString())
            .append("stateId", UUID.randomUUID().toString())
            .append("assignedTo", UUID.randomUUID().toString())
            .append("createdBy", UUID.randomUUID().toString())
            .append("createdAt", now.toString())
            .append("updatedAt", now.toString())

        val task = mapper.fromDocument(doc)

        assertThat(task.title).isEqualTo("Task")
    }
}