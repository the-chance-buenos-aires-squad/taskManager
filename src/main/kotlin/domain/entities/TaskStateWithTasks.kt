package domain.entities

data class TaskStateWithTasks(
    val state: TaskState,
    val tasks: List<Task>
)