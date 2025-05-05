import domain.entities.TaskState
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import presentation.cli.TaskState.TaskStatePrinter
import java.util.*

class GetAllTaskStatesCli(
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController,
) {

    fun getAllTaskStates() {

        uiController.printMessage(
            """

            ***   View All Task States  👀  ***
            
            """.trimIndent()
        )

        val allTaskStates = getAllTaskStatesUseCase.execute()

        if (allTaskStates.isEmpty()) {
            uiController.printMessage("No task states available.")
            return
        }

        TaskStatePrinter.printAllTaskStates(allTaskStates, uiController)
    }

}