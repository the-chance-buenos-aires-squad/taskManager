package presentation.cli.taskState

import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class GetAllTaskStatesCli(
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController,
) {

    suspend fun getAllTaskStates(selectedProjectId: UUID) {

        uiController.printMessage(
            HEADER
        )

        val allTaskStates = getAllTaskStatesUseCase.execute(selectedProjectId)

        if (allTaskStates.isEmpty()) {
            uiController.printMessage(EMPTY_STATES)
            return
        }

        TaskStatePrinter.printAllTaskStates(allTaskStates, uiController)
    }
    companion object Messages {
        const val HEADER = "===   View All Task States  ==="
        const val EMPTY_STATES = "No task states for this project."
    }
}