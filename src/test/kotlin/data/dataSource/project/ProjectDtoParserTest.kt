package data.dataSource.project

import com.google.common.truth.Truth.assertThat
import data.dto.ProjectDto
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProjectDtoParserTest {
    private val parser = ProjectDtoParser()
    val now: LocalDateTime = LocalDateTime.now()
    val id: UUID = UUID.randomUUID()

    private val expectedRow = ProjectDto(
        id.toString(),
        "Test Project",
        "This is a test project",
        now.toString()
    )

    val list = listOf(
        expectedRow._id,
        expectedRow.name,
        expectedRow.description,
        expectedRow.createdAt
    )

    @Test
    fun `mapEntityToRow should map Project to Project DTO`() {

        val actualRow = parser.toDto(list)

        assertThat(list[0]).isEqualTo(actualRow._id)
    }

    @Test
    fun `mapRowToEntity should map Project DTO to Project`() {

        val actualList = parser.fromDto(expectedRow)

        assertThat(expectedRow.name).isEqualTo(actualList[1])

    }
}