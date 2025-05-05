package data.dataSource.dummyData

import domain.entities.Task
import java.util.*

object DummyTasks {
    private val validTitle = "Valid Task Title"
    private val validDescription = "Valid Task Description"
    private val validProjectId = UUID.randomUUID()
    private val validStateId = UUID.randomUUID()
    private val validAssignedToId = UUID.randomUUID()
    private val validCreatedById = UUID.randomUUID()
    val validTask = Task(
        id = UUID.randomUUID(),
        validTitle,
        validDescription,
        validProjectId,
        validStateId,
        validAssignedToId,
        validCreatedById
    )
}