package presentation.cli.taskState

import domain.usecases.taskState.DeleteTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.UUID

class DeleteTaskStateCli(
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController
) {
    fun deleteTaskState(selectedProjectId: UUID) {

        uiController.printMessage(
            """
            
            ===   Delete Task State    🗑️   ===
            
            """.trimIndent()
        )

        val allTaskStates = getAllTaskStatesUseCase.execute(selectedProjectId)

        if (allTaskStates.isEmpty()) {
            uiController.printMessage("No task states available to delete.")
            return
        }

        TaskStatePrinter.printAllTaskStates(allTaskStates, uiController)

        uiController.printMessage("Enter the number of the task state to delete:")
        val taskIdInput = uiController.readInput().trim()

        val index = taskIdInput.toIntOrNull()

        if (index == null || index < 1 || index > allTaskStates.size) {
            uiController.printMessage("Invalid selection.")
            return
        }

        val stateId = allTaskStates[index - 1].id
        val result = deleteTaskStateUseCase.execute(stateId)

        uiController.printMessage(
            if (result) "Task state deleted successfully."
            else "No task state found with this ID."
        )
    }
}