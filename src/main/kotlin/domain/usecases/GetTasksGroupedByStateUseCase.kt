package domain.usecases

import com.sun.jdi.request.InvalidRequestStateException
import domain.entities.TaskStateWithTasks
import domain.usecases.taskState.GetAllTaskStatesUseCase

class GetTasksGroupedByStateUseCase(
    private val getTasksUseCase: GetTasksUseCase,
    private val getTaskStatesUseCase: GetAllTaskStatesUseCase
) {

    fun getTasksGroupedByState(): List<TaskStateWithTasks> {
        val allStates = getTaskStatesUseCase.execute()
        val allTasks = getTasksUseCase.getTasks()

        return allStates.map { state ->
            TaskStateWithTasks(
                state = state,
                tasks = allTasks.filter { it.stateId == state.id }
            )
        }

    }
}