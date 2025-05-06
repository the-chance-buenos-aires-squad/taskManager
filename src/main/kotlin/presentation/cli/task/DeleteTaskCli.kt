package presentation.cli.task

import domain.customeExceptions.UserNotLoggedInException
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.task.GetAllTasksUseCase
import presentation.UiController
import java.util.*

class DeleteTaskCli(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val uiController: UiController,
    private val authRepository: AuthRepository
) {

    fun delete(projectID: UUID) {
        val tasks = fetchProjectTasks(projectID)
        if (tasks.isEmpty()) return

        val selectedTask = selectTask(tasks) ?: return

        if (confirmDeletion(selectedTask.title)) {
            handleDeletion(selectedTask.id)
        } else {
            uiController.printMessage("Deletion canceled. Returning to dashboard.")
        }
    }

    private fun fetchProjectTasks(projectID: UUID): List<Task> {
        val tasks = getAllTasksUseCase.execute().filter { it.projectId == projectID }
        if (tasks.isEmpty()) {
            uiController.printMessage("No tasks found for this project.")
        } else {
            showTasks(tasks)
        }
        return tasks
    }

    private fun showTasks(tasks: List<Task>) {
        tasks.forEachIndexed { index, task ->
            uiController.printMessage("${index + 1} - ${task.title}")
        }
    }

    private fun selectTask(tasks: List<Task>): Task? {
        val index = promptForTaskIndex(tasks.size) ?: return null
        return tasks[index]
    }

    private fun promptForTaskIndex(taskCount: Int): Int? {
        repeat(2) {
            uiController.printMessage("Please choose task number: ", isInline = true)
            val input = uiController.readInput().toIntOrNull()
            if (input != null && input in 1..taskCount) return input - 1
        }
        uiController.printMessage("Invalid input. Returning to dashboard.")
        return null
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

    private fun handleDeletion(taskId: UUID) {
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