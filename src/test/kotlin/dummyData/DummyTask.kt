package dummyData

import java.time.LocalDateTime
import java.util.UUID

object DummyTask {

    val dummyTodoTask = createDummyTask(
        id = UUID.fromString("e7a1a8b0-51e2-4e71-b4f6-8c9f3e05b221"),
        title = "Task Todo Title",
        description = "task Todo description",
        projectId = UUID.randomUUID(),
        stateId = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c2f4e15b223"),
        assignedTo = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e053422"),
        createdBy = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b222"),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}