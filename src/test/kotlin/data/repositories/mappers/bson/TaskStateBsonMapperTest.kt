package data.repositories.mappers.bson

import com.google.common.truth.Truth.assertThat
import domain.entities.TaskState
import org.bson.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class TaskStateBsonMapperTest {
    private val mapper = TaskStateBsonMapper()

    @Test
    fun `toDocument should map TaskState to Document`() {
        val taskState = TaskState("1", "ToDo", "project123")
        val doc = mapper.toDocument(taskState)

        assertThat(doc.getString("name")).isEqualTo("ToDo")
    }

    @Test
    fun `fromDocument should map Document to TaskState`() {
        val doc = Document()
            .append("id", UUID.randomUUID().toString())
            .append("name", "InProgress")
            .append("projectId", UUID.randomUUID().toString())

        val state = mapper.fromDocument(doc)

        assertThat(state.name).isEqualTo("InProgress")
    }
}
