import domain.entities.State

fun createDummyTaskState(
    id: String,
    name: String,
    projectId: String
): State {
    return State(
        id = id,
        name = name,
        projectId = projectId
    )
}