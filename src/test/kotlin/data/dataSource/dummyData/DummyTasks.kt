package data.dataSource.dummyData

import data.dto.TaskDto
import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

object DummyTasks {

    val validTask = Task(
        id = UUID.randomUUID(),
        title = "Valid Task Title",
        description = "Valid Task Description",
        projectId = UUID.randomUUID(),
        stateId = UUID.randomUUID(),
        assignedTo = UUID.randomUUID().toString(),
        createdBy = UUID.randomUUID(),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
    val validTaskDto = TaskDto(
        id = validTask.id.toString(),
        title = validTask.title,
        description = validTask.description,
        projectId = validTask.projectId.toString(),
        stateId = validTask.stateId.toString(),
        assignedTo = validTask.assignedTo.toString(),
        createdBy = validTask.createdBy.toString(),
        createdAt = validTask.createdAt.toString(),
        updatedAt = validTask.updatedAt.toString()

    )
}