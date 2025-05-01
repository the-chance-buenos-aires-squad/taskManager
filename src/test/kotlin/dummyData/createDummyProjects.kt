package dummyData


import domain.entities.Project
import java.time.LocalDateTime

fun createDummyProject(
    id: String = "123",
    name: String = "Test Project"
) = Project(
    id = id,
    name = name,
    description = "desc",
    createdAt = LocalDateTime.now()
)
