package dummyData.dummyStateData

import data.dto.TaskStateDto
import domain.entities.TaskState
import java.util.*


object DummyTaskState {
    val todoDto = TaskStateDto(
        _id = UUID.fromString("00000000-1000-0000-0000-000000000000").toString(),
        name = "To Do",
        projectId = UUID.fromString("10000000-1000-0000-0000-000000000000").toString()
    )

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
        projectId = UUID.fromString("30000000-1000-0000-0000-000000000000")
    )

    val blocked = TaskState(
        id = UUID.fromString("00000000-4000-0000-0000-000000000000"),
        name = "Blocked",
        projectId = UUID.fromString("40000000-1000-0000-0000-000000000000")
    )

    val blockedDto = TaskStateDto(
        _id = UUID.fromString("00000000-4000-0000-0000-000000000000").toString(),
        name = "Blocked",
        projectId = UUID.fromString("40000000-1000-0000-0000-000000000000").toString()
    )

    val readyForReview = TaskState(
        id = UUID.fromString("50000000-1000-0000-0000-000000000000"),
        name = "Ready For Review",
        projectId = UUID.fromString("50000000-1000-0000-0000-000000000000")
    )

    val readyForReviewDto = TaskStateDto(
        _id = UUID.fromString("50000000-1000-0000-0000-000000000000").toString(),
        name = "Ready For Review",
        projectId = UUID.fromString("50000000-1000-0000-0000-000000000000").toString()
    )

}