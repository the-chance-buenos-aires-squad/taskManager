package data.dataSource.project

import com.google.common.truth.Truth.assertThat
import data.dto.ProjectDto
import dummyData.DummyProjects
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProjectDtoParserTest {
    private val parser = ProjectDtoParser()

    @Test
    fun `mapEntityToRow should map Project to Project DTO`() {

        val actualRow = parser.toDto(DummyProjects.list)

        assertThat(DummyProjects.list[0]).isEqualTo(actualRow._id)
    }

    @Test
    fun `mapRowToEntity should map Project DTO to Project`() {

        val actualList = parser.fromDto(DummyProjects.expectedRow)

        assertThat(DummyProjects.expectedRow.name).isEqualTo(actualList[1])

    }
}