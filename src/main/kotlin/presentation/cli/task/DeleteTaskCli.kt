package presentation.cli.task

import presentation.exceptions.UserNotLoggedInException
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.task.GetAllTasksUseCase
import presentation.UiController
import java.util.*

class DeleteTaskCli(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val uiController: UiController,
) {

    suspend fun delete(projectID: UUID) {
        val allTasks = getAllTasksUseCase.execute()
        val tasks = TaskCliUtils.fetchProjectTasks(allTasks, projectID, uiController)
        if (tasks.isEmpty()) return

        val selectedTask = TaskCliUtils.selectTask(tasks, uiController) ?: return

        if (confirmDeletion(selectedTask.title)) {
            handleDeletion(selectedTask.id)
        } else {
            uiController.printMessage(DELETION_CANCELLED)
        }
    }

    private fun confirmDeletion(taskTitle: String): Boolean {
        repeat(2) {
            uiController.printMessage("$ARE_YOU_SURE$taskTitle?")
            uiController.printMessage(CONFIRM_PROMPT)
            when (uiController.readInput().lowercase()) {
                YES -> return true
                NO  -> return false
            }
        }
        uiController.printMessage(INVALID_CONFIRMATION)
        return false
    }

    private suspend fun handleDeletion(taskId: UUID) {
        try {
            val success = deleteTaskUseCase.execute(taskId)
            val message = if (success) {
                SUCCESS
            } else {
                FAILURE
            }
            uiController.printMessage(message)
        } catch (e: UserNotLoggedInException) {
            uiController.printMessage(e.message.toString())
        }
    }

    companion object Messages {
        const val CONFIRM_PROMPT = "Reply 'yes' or 'no':"
        const val DELETION_CANCELLED = "Deletion canceled. Returning to dashboard."
        const val INVALID_CONFIRMATION = "Invalid confirmation. Returning to dashboard."
        const val SUCCESS = "Task deleted successfully."
        const val FAILURE = "Error: Task was not deleted."
        const val YES = "yes"
        const val NO = "no"
        const val ARE_YOU_SURE = "Are you sure you want to delete the task: "
    }
}