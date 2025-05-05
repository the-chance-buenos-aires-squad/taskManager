package dummyData.dummyStateData

import domain.entities.TaskState
import java.util.*


object DummyTaskState {
    val todo = TaskState(
        id = UUID.randomUUID(),
        name = "To Do",
        projectId = "P001"
    )

    val inProgress = TaskState(
        id = UUID.randomUUID(),
        name = "In Progress",
        projectId = "P002"
    )

    val done = TaskState(
        id = UUID.randomUUID(),
        name = "Done",
        projectId = "P003"
    )

    val blocked = TaskState(
        id = UUID.randomUUID(),
        name = "Blocked",
        projectId = "P004"
    )

    val readyForReview = TaskState(
        id = UUID.randomUUID(),
        name = "Ready For Review",
        projectId = "P005"
    )

}
