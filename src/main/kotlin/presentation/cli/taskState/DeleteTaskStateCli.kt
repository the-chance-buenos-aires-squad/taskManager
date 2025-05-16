package presentation.cli.taskState

import domain.usecases.taskState.DeleteTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class DeleteTaskStateCli(
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController
) {
    suspend fun deleteTaskState(selectedProjectId: UUID) {

        uiController.printMessage(
            HEADER
        )

        val allTaskStates = getAllTaskStatesUseCase.execute(selectedProjectId)

        if (allTaskStates.isEmpty()) {
            uiController.printMessage(EMPTY_STATES)
            return
        }

        TaskStatePrinter.printAllTaskStates(allTaskStates, uiController)

        uiController.printMessage(INDEX_PROMPT)
        val taskIdInput = uiController.readInput().trim()

        val index = taskIdInput.toIntOrNull()

        if (index == null || index < 1 || index > allTaskStates.size) {
            uiController.printMessage(INVALID_SELECTION)
            return
        }

        val stateId = allTaskStates[index - 1].id
        val result = deleteTaskStateUseCase.execute(stateId)

        uiController.printMessage(
            if (result) SUCCESS
            else FAILURE
        )
    }
    companion object Messages {
        const val HEADER = "===   Delete Task State   ️  ==="
        const val EMPTY_STATES = "No task states available to delete."
        const val INDEX_PROMPT = "Enter the number of the task state to delete:"
        const val INVALID_SELECTION = "Invalid selection."
        const val SUCCESS = "Task state deleted successfully."
        const val FAILURE = "No task state found with this ID."
    }
}