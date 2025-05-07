package presentation.cli.taskState

import domain.usecases.taskState.EditTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class EditTaskStateCli(
    private val editTaskStateUseCase: EditTaskStateUseCase,
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController,
) {
    private val inputHandler = TaskStateInputHandler(uiController)

    fun editTaskState(selectedProjectId: UUID) {
        uiController.printMessage(
            """

            ===   Edit Task State   ✏️      ===
            
            """.trimIndent()
        )

        val allTaskStates = getAllTaskStatesUseCase.execute(selectedProjectId)

        if (allTaskStates.isEmpty()) {
            uiController.printMessage("No task states available to edit.")
            return
        }

        TaskStatePrinter.printAllTaskStates(allTaskStates, uiController)

        uiController.printMessage("Enter the number of the task state to edit:")
        val input = uiController.readInput().trim()

        val index = input.toIntOrNull()?.minus(1)
        if (index == null || index !in allTaskStates.indices) {
            uiController.printMessage("Invalid selection.")
            return
        }

        val selectedTaskState = allTaskStates[index]

        val updatedState = inputHandler.readAndValidateUserInputs(
            isEdit = true,
            existingId = selectedTaskState.id,
            projectId = selectedProjectId
        )

        val newStateEditing = editTaskStateUseCase.execute(updatedState)

        if (newStateEditing) {
            uiController.printMessage("Task state edited successfully.")
            uiController.printMessage(
                """
                Updated Task State ${index + 1}: Name: ${updatedState.name}
                """.trimIndent()
            )
        } else uiController.printMessage("Failed to edit task state.")
    }
}