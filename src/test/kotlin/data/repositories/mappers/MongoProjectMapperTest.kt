package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import data.dto.ProjectDto
import domain.entities.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class MongoProjectMapperTest {
    private val mapper = MongoProjectMapper()

    @Test
    fun `mapEntityToRow should map Project to Project DTO`() {
        val now = LocalDateTime.now()
        val id = UUID.randomUUID()
        val project = Project(
            id = id,
            name = "Test Project",
            description = "This is a test project",
            createdAt = now
        )

        val expectedRow = ProjectDto(
            id.toString(),
            "Test Project",
            "This is a test project",
            now.toString()
        )

        val actualRow = mapper.toMap(project)

        assertThat(expectedRow.id).isEqualTo(actualRow.id)
    }

    @Test
    fun `mapRowToEntity should map Project DTO to Project`() {
        val now = LocalDateTime.now()
        val id = UUID.randomUUID()
        val row = ProjectDto(
            id.toString(),
            "Test Project",
            "This is a test project",
            now.toString()
        )

        val expectedProject = Project(
            id = id,
            name = "Test Project",
            description = "This is a test project",
            createdAt = now
        )

        val actualProject = mapper.fromMap(row)
        assertThat(expectedProject.name).isEqualTo(actualProject.name)

    }
}
