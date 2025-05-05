import domain.entities.TaskState
import java.util.*

fun createDummyTaskState(
    id: String,
    name: String,
    projectId: String
): TaskState {
    return TaskState(
        id = UUID.randomUUID(),
        name = name,
        projectId = projectId
    )
}