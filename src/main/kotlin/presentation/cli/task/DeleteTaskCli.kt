package presentation.cli.task

import domain.repositories.AuthRepository
import domain.usecases.task.DeleteTaskUseCase
import presentation.UiController
import java.util.UUID
import domain.entities.Task
import domain.usecases.task.GetAllTasksUseCase

class DeleteTaskCli(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val uiController: UiController,
    private val authRepository: AuthRepository
) {

    fun delete() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            uiController.printMessage(" You are not logged in.")
            return
        }


        val tasks: List<Task> = try {
            getAllTasksUseCase.execute()
        } catch (e: Exception) {
            uiController.printMessage(" Failed to load tasks.")
            return
        }

        if (tasks.isEmpty()) {
            uiController.printMessage(" No tasks found.")
            return
        }

        uiController.printMessage(" Select a task to delete:")
        tasks.forEachIndexed { index, task ->
            uiController.printMessage("${index + 1}. ${task.title}")
        }

        uiController.printMessage(" Enter the task number to delete:")
        val index = try {
            val input = uiController.readInput().trim()
            val parsedIndex = input.toIntOrNull()
            if (parsedIndex == null || parsedIndex <= 0 || parsedIndex > tasks.size) {
                uiController.printMessage(" Invalid input. Please enter a valid task number.")
                return
            }
            parsedIndex
        } catch (e: Exception) {
            uiController.printMessage(" Error while reading input.")
            return
        }

        val selectedTask = tasks[index - 1]
        uiController.printMessage(" Are you sure you want to delete: ${selectedTask.title}? (yes/no)")
        val confirmation = uiController.readInput().trim().lowercase()

        if (confirmation == "yes") {
            val deleted = try {
                deleteTaskUseCase.deleteTask(selectedTask.id)
            } catch (e: Exception) {
                uiController.printMessage(" Failed to delete task: ${e.message}")
                return
            }

            if (deleted) {
                uiController.printMessage(" Task deleted.")
            } else {
                uiController.printMessage(" Task not found.")
            }

        }
    }
}
