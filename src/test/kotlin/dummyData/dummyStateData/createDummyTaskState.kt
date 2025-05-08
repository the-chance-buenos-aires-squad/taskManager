import domain.entities.TaskState
import java.util.*

fun createDummyTaskState(
    id: UUID = UUID.randomUUID(),
    name: String,
    projectId: UUID = UUID.randomUUID()
): TaskState {
    return TaskState(
        id = id,
        name = name,
        projectId = projectId
    )
}