package dummyData

import data.dto.TaskDto
import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

object DummyTasks {

    private val fixedTaskId = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b223")
    private val fixedProjectId = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b224")
    private val fixedStateId = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b225")
    private val fixedAssignedToId = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b226")
    private val fixedCreatedById = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b227")
    private val fixedCreatedAt = LocalDateTime.parse("2023-01-01T10:00:00")
    private val fixedUpdatedAt = LocalDateTime.parse("2023-01-01T10:30:00")

    val validTask = Task(
        id = fixedTaskId,
        title = "Valid Task Title",
        description = "Valid Task Description",
        projectId = fixedProjectId,
        stateId = fixedStateId,
        assignedTo = fixedAssignedToId.toString(),
        createdBy = fixedCreatedById,
        createdAt = fixedCreatedAt,
        updatedAt = fixedUpdatedAt
    )

    val unassignedTask = validTask.copy(
        id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b228"),
        title = "Unassigned Task",
        assignedTo = null
    )

    val minimalTask = Task(
        id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b229"),
        title = "Minimal Task",
        description = "Minimal Description",
        projectId = fixedProjectId,
        stateId = fixedStateId,
        assignedTo = null,
        createdBy = fixedCreatedById
    )

    val validTaskDto = TaskDto(
        id = validTask.id.toString(),
        title = validTask.title,
        description = validTask.description,
        projectId = validTask.projectId.toString(),
        stateId = validTask.stateId.toString(),
        assignedTo = validTask.assignedTo?.toString(),
        createdBy = validTask.createdBy.toString(),
        createdAt = validTask.createdAt.toString(),
        updatedAt = validTask.updatedAt.toString()
    )

    val unassignedTaskDto = TaskDto(
        id = unassignedTask.id.toString(),
        title = unassignedTask.title,
        description = unassignedTask.description,
        projectId = unassignedTask.projectId.toString(),
        stateId = unassignedTask.stateId.toString(),
        assignedTo = null,
        createdBy = unassignedTask.createdBy.toString(),
        createdAt = unassignedTask.createdAt.toString(),
        updatedAt = unassignedTask.updatedAt.toString()
    )

    val validTaskRow = listOf(
        validTask.id.toString(),
        validTask.title,
        validTask.description,
        validTask.projectId.toString(),
        validTask.stateId.toString(),
        validTask.assignedTo.toString(),
        validTask.createdBy.toString(),
        validTask.createdAt.toString(),
        validTask.updatedAt.toString()
    )

    val unassignedTaskRow = listOf(
        unassignedTask.id.toString(),
        unassignedTask.title,
        unassignedTask.description,
        unassignedTask.projectId.toString(),
        unassignedTask.stateId.toString(),
        "",
        unassignedTask.createdBy.toString(),
        unassignedTask.createdAt.toString(),
        unassignedTask.updatedAt.toString()
    )
}