package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import domain.entities.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ProjectMapperTest {
    private val mapper = ProjectMapper()

    @Test
    fun `mapEntityToRow should map Project to List of Strings`() {
        val now = LocalDateTime.now()
        val project = Project(
            id = "1",
            name = "Test Project",
            description = "This is a test project",
            createdAt = now
        )

        val expectedRow = listOf(
            "1",
            "Test Project",
            "This is a test project",
            now.toString()
        )

        val actualRow = mapper.mapEntityToRow(project)

        assertThat(expectedRow).isEqualTo(actualRow)
    }

    @Test
    fun `mapRowToEntity should map List of Strings to Project`() {
        val now = LocalDateTime.now()
        val row = listOf(
            "1",
            "Test Project",
            "This is a test project",
            now.toString()
        )

        val expectedProject = Project(
            id = "1",
            name = "Test Project",
            description = "This is a test project",
            createdAt = now
        )

        val actualProject = mapper.mapRowToEntity(row)
        assertThat(expectedProject).isEqualTo(actualProject)

    }
}