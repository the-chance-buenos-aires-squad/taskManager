package dummyData

import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

fun createDummyTask(
    id: UUID = UUID.randomUUID(),
    title: String,
    description: String = "task description",
    projectId: UUID,
    stateId: UUID = UUID.randomUUID(),
    assignedTo: UUID? = UUID.randomUUID(),
    createdBy: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now()
) = Task(
    id = id,
    title = title,
    description = description,
    projectId = projectId,
    stateId = stateId,
    assignedTo = assignedTo.toString(),
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
