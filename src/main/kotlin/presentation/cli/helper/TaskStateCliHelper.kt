package presentation.cli.helper

import domain.entities.TaskState
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper.Companion.EXCEPTION_MESSAGE
import presentation.cli.helper.ProjectCliHelper.Companion.INVALID_INPUT_MESSAGE
import java.util.UUID

class TaskStateCliHelper(
    private val getAllStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController
) {

    suspend fun getTaskStates(projectId: UUID): List<TaskState> {
        return try {
            getAllStatesUseCase.execute(projectId)
        } catch (ex: Exception) {
            uiController.printMessage(EXCEPTION_MESSAGE.format(ex.message))
            emptyList()
        }
    }

    fun selectTaskState(taskState: List<TaskState>): TaskState? {
        if (taskState.isEmpty()) return null

        uiController.printMessage(SELECT_TASK_STATE_MESSAGE)
        taskState.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1}. ${project.title}")
        }

        return selectProjectFromList(taskState)
    }

    private fun selectProjectFromList(taskState: List<TaskState>): TaskState {
        while (true) {
            uiController.printMessage(ENTER_TASK_STATE_MESSAGE, true)
            val input = uiController.readInput().trim()

            when (val selectedIndex = input.toIntOrNull()?.minus(1)) {
                null -> uiController.printMessage(INVALID_INPUT_MESSAGE)
                else -> {
                    if (selectedIndex in taskState.indices) {
                        return taskState[selectedIndex]
                    }
                    uiController.printMessage("$INVALID_INPUT_MESSAGE from the menu.")
                }
            }
        }
    }


    companion object {
        const val ENTER_TASK_STATE_MESSAGE = "Enter Task State number: "
        const val SELECT_TASK_STATE_MESSAGE = "Select Task State number: "
    }


}