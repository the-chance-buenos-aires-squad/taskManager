import org.buinos.domain.entities.State

fun createDummyState(
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