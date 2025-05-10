package dummyData


import data.dto.ProjectDto
import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

fun createDummyProject(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Project",
    description: String = "desc",
) = Project(
    id = id,
    name = name,
    description = description,
    createdAt = LocalDateTime.now()
)

object DummyProjects {

    val now: String = LocalDateTime.now().toString()
    val id: UUID = UUID.randomUUID()

    val projectDto1 = ProjectDto(
        _id = UUID.randomUUID().toString(),
        name = "Project Alpha",
        description = "Test project for Alpha",
        createdAt = now
    )

    val projectDto2 = ProjectDto(
        _id = UUID.randomUUID().toString(),
        name = "Project Beta",
        description = "Test project for Beta",
        createdAt = now
    )

    val rawProject1 = listOf(
        projectDto1._id,
        projectDto1.name,
        projectDto1.description,
        projectDto1.createdAt
    )

    val rawProject2 = listOf(
        projectDto2._id,
        projectDto2.name,
        projectDto2.description,
        projectDto2.createdAt
    )

    val projectList = listOf(projectDto1, projectDto2)
    val rowList = listOf(rawProject1, rawProject2)

    val expectedRow = ProjectDto(
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
}
