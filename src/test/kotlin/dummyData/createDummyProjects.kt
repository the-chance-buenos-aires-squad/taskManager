package dummyData


import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

fun createDummyProject(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Project"
) = Project(
    id = id,
    name = name,
    description = "desc",
    createdAt = LocalDateTime.now()
)
