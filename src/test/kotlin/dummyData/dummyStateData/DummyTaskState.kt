package dummyData.dummyStateData

import domain.entities.TaskState
import java.util.UUID


object DummyTaskState {
    val todo = TaskState(
        id = UUID.fromString("00000000-1000-0000-0000-000000000000"),
        name = "To Do",
        projectId = UUID.fromString("10000000-1000-0000-0000-000000000000")
    )

    val inProgress = TaskState(
        id = UUID.fromString("00000000-2000-0000-0000-000000000000"),
        name = "In Progress",
        projectId = UUID.fromString("20000000-1000-0000-0000-000000000000")
    )

    val done = TaskState(
        id = UUID.fromString("00000000-3000-0000-0000-000000000000"),
        name = "Done",
        projectId =UUID.fromString("30000000-1000-0000-0000-000000000000")
    )

    val blocked = TaskState(
        id = UUID.fromString("00000000-4000-0000-0000-000000000000"),
        name = "Blocked",
        projectId = UUID.fromString("40000000-1000-0000-0000-000000000000")
    )

    val readyForReview = TaskState(
        id = UUID.fromString("50000000-1000-0000-0000-000000000000"),
        name = "Ready For Review",
        projectId = UUID.fromString("50000000-1000-0000-0000-000000000000")
    )

}
