package domain.usecases

import domain.entities.Project
import domain.entities.TaskStateWithTasks
import domain.usecases.taskState.GetAllTaskStatesUseCase

class GetTasksGroupedByStateUseCase(
    private val getTasksUseCase: GetTasksUseCase,
    private val getTaskStatesUseCase: GetAllTaskStatesUseCase
) {

    fun getTasksGroupedByState(project: Project): List<TaskStateWithTasks> {
        val allStates = getTaskStatesUseCase.execute().sortedBy { it.id }
        val allTasks = getTasksUseCase.getTasks().filter { it.projectId == project.id }

        return allStates.map { state ->
            TaskStateWithTasks(
                state = state,
                tasks = allTasks.filter { it.stateId == state.id }
            )
        }

    }
}