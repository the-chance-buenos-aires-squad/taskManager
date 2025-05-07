package presentation.cli.task

import domain.customeExceptions.UserNotLoggedInException
import domain.entities.Task
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
            uiController.printMessage("Deletion canceled. Returning to dashboard.")
        }
    }

    private fun confirmDeletion(taskTitle: String): Boolean {
        repeat(2) {
            uiController.printMessage("Are you sure you want to delete the task: $taskTitle?")
            uiController.printMessage("Reply 'yes' or 'no':")
            when (uiController.readInput().lowercase()) {
                "yes" -> return true
                "no" -> return false
            }
        }
        uiController.printMessage("Invalid confirmation. Returning to dashboard.")
        return false
    }

    private suspend fun handleDeletion(taskId: UUID) {
        try {
            val success = deleteTaskUseCase.deleteTask(taskId)
            val message = if (success) {
                "Task deleted successfully."
            } else {
                "Error: Task was not deleted."
            }
            uiController.printMessage(message)
        } catch (e: UserNotLoggedInException) {
            uiController.printMessage(e.message.toString())
        }
    }
}