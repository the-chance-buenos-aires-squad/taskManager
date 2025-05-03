package dummyData.dummyStateData

import domain.entities.TaskState


object DummyTaskState {
    val todo = TaskState(
        id = "1",
        name = "To Do",
        projectId = "P001"
    )

    val inProgress = TaskState(
        id = "2",
        name = "In Progress",
        projectId = "P002"
    )

    val done = TaskState(
        id = "3",
        name = "Done",
        projectId = "P003"
    )

    val blocked = TaskState(
        id = "4",
        name = "Blocked",
        projectId = "P004"
    )

    val readyForReview = TaskState(
        id = "5",
        name = "Ready For Review",
        projectId = "P005"
    )

}
