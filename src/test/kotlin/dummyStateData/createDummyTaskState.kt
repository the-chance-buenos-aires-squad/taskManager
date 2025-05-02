import domain.entities.TaskState

fun createDummyTaskState(
    id: String,
    name: String,
    projectId: String
): TaskState {
    return TaskState(
        id = id,
        name = name,
        projectId = projectId
    )
}