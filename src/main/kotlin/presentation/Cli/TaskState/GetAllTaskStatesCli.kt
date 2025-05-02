package presentation.Cli.TaskState

import domain.entities.TaskState
import domain.usecases.GetAllTaskStatesUseCase

class GetAllTaskStatesCli(
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase
) {
    fun getAllTaskStates(): List<TaskState> {
        return getAllTaskStatesUseCase.execute()
    }
}