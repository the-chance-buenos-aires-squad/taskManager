package domain.usecases

import domain.entities.Project
import domain.entities.Task
import domain.entities.TaskState
import domain.usecases.task.GetTasksUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase

class GetTasksGroupedByStateUseCase(
    private val getTasksUseCase: GetTasksUseCase,
    private val getTaskStatesUseCase: GetAllTaskStatesUseCase
) {

    suspend fun execute(project: Project): List<TaskStateWithTasks> {
        val allStates = getTaskStatesUseCase.execute(project.id)
        val allTasks = getTasksUseCase.execute().filter { it.projectId == project.id }

        return allStates.map { state ->
            TaskStateWithTasks(
                state = state,
                tasks = allTasks.filter { it.stateId == state.id }
            )
        }
    }

    data class TaskStateWithTasks(
        val state: TaskState,
        val tasks: List<Task>
    )
}