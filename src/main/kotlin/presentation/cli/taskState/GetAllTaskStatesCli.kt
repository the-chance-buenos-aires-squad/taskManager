package presentation.cli.taskState

import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class GetAllTaskStatesCli(
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController,
) {

    fun getAllTaskStates(selectedProjectId: UUID) {

        uiController.printMessage(
            """

            ===   View All Task States  👀  ===
            
            """.trimIndent()
        )

        val allTaskStates = getAllTaskStatesUseCase.execute(selectedProjectId)

        if (allTaskStates.isEmpty()) {
            uiController.printMessage("No task states for this project.")
            return
        }

        TaskStatePrinter.printAllTaskStates(allTaskStates, uiController)
    }
}