package dummyData


import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

fun createDummyProject(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Project",
    description : String = "desc",
) = Project(
    id = id,
    name = name,
    description =description,
    createdAt = LocalDateTime.now()
)