package presentation.Cli.TaskState

import domain.entities.TaskState
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController

class GetAllTaskStatesCli(
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController,
) {

    fun getAllTaskStates() {
        val states = getAllTaskStatesUseCase.execute()

        if (states.isEmpty()) {
            uiController.printMessage("No task states found.")
            return
        }

        uiController.printMessage("List of task states:")
        states.forEach { state ->
            uiController.printMessage(
                "ID: ${state.id}, Name: ${state.name}, Project ID: ${state.projectId}"
            )
        }
    }
}