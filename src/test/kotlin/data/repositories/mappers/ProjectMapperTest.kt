package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import domain.entities.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class ProjectMapperTest {
    private val mapper = ProjectMapper()

    @Test
    fun `mapEntityToRow should map Project to List of Strings`() {
        val now = LocalDateTime.now()
        val id = UUID.randomUUID()
        val project = Project(
            id = id,
            name = "Test Project",
            description = "This is a test project",
            createdAt = now
        )

        val expectedRow = listOf(
            id.toString(),
            "Test Project",
            "This is a test project",
            now.toString()
        )

        val actualRow = mapper.mapEntityToRow(project)

        assertThat(expectedRow[1]).isEqualTo(actualRow[1])
    }

    @Test
    fun `mapRowToEntity should map List of Strings to Project`() {
        val now = LocalDateTime.now()
        val id = UUID.randomUUID()
        val row = listOf(
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

        val actualProject = mapper.mapRowToEntity(row)
        assertThat(expectedProject.name).isEqualTo(actualProject.name)

    }
}