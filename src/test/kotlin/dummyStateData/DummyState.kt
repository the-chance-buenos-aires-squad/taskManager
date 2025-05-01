package dummyStateData

import domain.entities.State


object DummyState {
    val todo = State(
        id = "1",
        name = "To Do",
        projectId = "P001"
    )

    val inProgress = State(
        id = "2",
        name = "In Progress",
        projectId = "P002"
    )

    val done = State(
        id = "3",
        name = "Done",
        projectId = "P003"
    )

    val blocked = State(
        id = "4",
        name = "Blocked",
        projectId = "P004"
    )

    val readyForReview = State(
        id = "5",
        name = "Ready For Review",
        projectId = "P005"
    )

}
