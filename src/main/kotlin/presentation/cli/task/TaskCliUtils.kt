package presentation.cli.task

import domain.entities.Task
import domain.entities.TaskState
import domain.repositories.UserRepository
import presentation.UiController
import java.util.*

object TaskCliUtils {

    fun fetchProjectTasks(
        allTasks: List<Task>,
        projectID: UUID,
        uiController: UiController
    ): List<Task> {
        val tasks = allTasks.filter { it.projectId == projectID }
        if (tasks.isEmpty()) {
            uiController.printMessage("No tasks found for this project.")
        } else {
            showTasks(tasks, uiController)
        }
        return tasks
    }

    fun showTasks(tasks: List<Task>, uiController: UiController) {
        tasks.forEachIndexed { index, task ->
            uiController.printMessage("${index + 1} - ${task.title}")
        }
    }

    fun selectTask(
        tasks: List<Task>,
        uiController: UiController
    ): Task? {
        val index = promptForTaskIndex(tasks.size, uiController) ?: return null
        return tasks[index]
    }

    fun promptForTaskIndex(
        taskCount: Int,
        uiController: UiController
    ): Int? {
        repeat(2) {
            uiController.printMessage("Please choose task number: ", isInline = true)
            val input = uiController.readInput().toIntOrNull()
            if (input != null && input in 1..taskCount) return input - 1
        }
        uiController.printMessage("Invalid input. Returning to dashboard.")
        return null
    }

    suspend fun promptForUpdatedTask(
        task: Task,
        projectStates: List<TaskState>,
        userRepository: UserRepository,
        uiController: UiController
    ): Task {
        uiController.printMessage("Leave fields empty to keep current values.\n")

        val newTitle = promptForTitle(task.title, uiController)
        val newDescription = promptForDescription(task.description, uiController)
        val newStateId = promptForStateId(task.stateId, projectStates, uiController)
        val newAssignedTo = promptForAssignedTo(task.assignedTo, userRepository, uiController)

        return task.copy(
            title = newTitle,
            description = newDescription,
            stateId = newStateId,
            assignedTo = newAssignedTo,
            updatedAt = java.time.LocalDateTime.now()
        )
    }

    private fun promptForTitle(current: String, ui: UiController): String {
        ui.printMessage("Enter new title (current: $current):")
        return ui.readInput().takeIf { it.isNotBlank() } ?: current
    }

    private fun promptForDescription(current: String, ui: UiController): String {
        ui.printMessage("Enter new description (current: $current):")
        return ui.readInput().takeIf { it.isNotBlank() } ?: current
    }

    private fun promptForStateId(
        currentStateId: UUID,
        states: List<TaskState>,
        ui: UiController
    ): UUID {
        val currentStateName = states.find { it.id == currentStateId }?.title ?: "Unknown"
        ui.printMessage("Available States:")
        states.forEachIndexed { index, state ->
            ui.printMessage("${index + 1}. ${state.title}")
        }

        ui.printMessage("Select state index (current: $currentStateName):")
        val input = ui.readInput()
        val index = input.toIntOrNull()?.takeIf { it in 1..states.size }

        return index?.let { states[it - 1].id } ?: currentStateId
    }

    private suspend fun promptForAssignedTo(
        current: UUID?,
        userRepo: UserRepository,
        ui: UiController
    ): UUID? {
        ui.printMessage("Enter username to assign task to (current: ${current ?: "None"}):")
        val input = ui.readInput()
        if (input.isBlank()) return current

        val user = userRepo.getUserByUserName(input)
        return if (user != null) {
            user.id
        } else {
            ui.printMessage("User not found. Keeping existing assignment.")
            current
        }
    }
}