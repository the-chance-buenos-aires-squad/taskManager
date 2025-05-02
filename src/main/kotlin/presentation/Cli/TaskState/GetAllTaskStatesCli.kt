package presentation.Cli.TaskState

import domain.entities.TaskState
import domain.usecases.taskState.GetAllTaskStatesUseCase

class GetAllTaskStatesCli(
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase
) {
    fun getAllTaskStates(): List<TaskState> {
        return getAllTaskStatesUseCase.execute()
    }
}