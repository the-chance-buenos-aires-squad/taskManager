package data.repositories.mappers.bson

import com.google.common.truth.Truth.assertThat
import domain.entities.Project
import org.bson.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProjectBsonMapperTest {
    private val mapper = ProjectBsonMapper()

    @Test
    fun `toDocument should map Project to Document`() {
        val now = LocalDateTime.now()
        val project = Project(UUID.randomUUID(), "Project Name", "Description", now)

        val document = mapper.toDocument(project)

        assertThat(document.getString("name")).isEqualTo(project.name)
    }

    @Test
    fun `fromDocument should map Document to Project`() {
        val now = LocalDateTime.now()
        val id = UUID.randomUUID().toString()
        val doc = Document()
            .append("id", id)
            .append("name", "Project Name")
            .append("description", "Description")
            .append("createdAt", now.toString())

        val project = mapper.fromDocument(doc)

        assertThat(project.name).isEqualTo("Project Name")
    }
}