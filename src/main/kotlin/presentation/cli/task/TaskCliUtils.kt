package presentation.cli.task

import domain.entities.Task
import domain.entities.TaskState
import domain.repositories.UserRepository
import presentation.UiController
import presentation.cli.task.TaskCliUtils.Messages.AVAILABLE_STATES
import presentation.cli.task.TaskCliUtils.Messages.EDIT_HINT
import presentation.cli.task.TaskCliUtils.Messages.INVALID_INDEX
import presentation.cli.task.TaskCliUtils.Messages.NO_TASKS_FOUND
import presentation.cli.task.TaskCliUtils.Messages.PROMPT_ASSIGN_USER
import presentation.cli.task.TaskCliUtils.Messages.PROMPT_DESCRIPTION
import presentation.cli.task.TaskCliUtils.Messages.PROMPT_TASK_INDEX
import presentation.cli.task.TaskCliUtils.Messages.PROMPT_TITLE
import presentation.cli.task.TaskCliUtils.Messages.SELECT_STATE_INDEX
import presentation.cli.task.TaskCliUtils.Messages.UNKNOWN
import presentation.cli.task.TaskCliUtils.Messages.USER_NOT_FOUND
import java.util.*

object TaskCliUtils {

    fun fetchProjectTasks(
        allTasks: List<Task>,
        projectID: UUID,
        uiController: UiController
    ): List<Task> {
        val tasks = allTasks.filter { it.projectId == projectID }
        if (tasks.isEmpty()) {
            uiController.printMessage(NO_TASKS_FOUND)
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
            uiController.printMessage(PROMPT_TASK_INDEX, isInline = true)
            val input = uiController.readInput().toIntOrNull()
            if (input != null && input in 1..taskCount) return input - 1
        }
        uiController.printMessage(INVALID_INDEX)
        return null
    }


    private fun promptForTitle(current: String, ui: UiController): String {
        ui.printMessage(PROMPT_TITLE.format(current))
        return ui.readInput().takeIf { it.isNotBlank() } ?: current
    }

    private fun promptForDescription(current: String, ui: UiController): String {
        ui.printMessage(PROMPT_DESCRIPTION.format(current))
        return ui.readInput().takeIf { it.isNotBlank() } ?: current
    }

    private fun promptForStateId(
        currentStateId: UUID,
        states: List<TaskState>,
        ui: UiController
    ): UUID {
        val currentStateName = states.find { it.id == currentStateId }?.title ?: UNKNOWN
        ui.printMessage(AVAILABLE_STATES)
        states.forEachIndexed { index, state ->
            ui.printMessage("${index + 1}. ${state.title}")
        }

        ui.printMessage(SELECT_STATE_INDEX.format(currentStateName))
        val input = ui.readInput()
        val index = input.toIntOrNull()?.takeIf { it in 1..states.size }

        return index?.let { states[it - 1].id } ?: currentStateId
    }

    private suspend fun promptForAssignedTo(
        current: UUID?,
        userRepo: UserRepository,
        ui: UiController
    ): UUID? {
        ui.printMessage(PROMPT_ASSIGN_USER.format(current ?: "None"))
        val input = ui.readInput()
        if (input.isBlank()) return current

        val user = userRepo.getUserByUserName(input)
        return if (user != null) {
            UUID.fromString(user.id)
        } else {
            ui.printMessage(USER_NOT_FOUND)
            current
        }
    }

    private object Messages {
        const val NO_TASKS_FOUND = "No tasks found for this project."
        const val PROMPT_TASK_INDEX = "Please choose task number: "
        const val INVALID_INDEX = "Invalid input. Returning to dashboard."
        const val EDIT_HINT = "Leave fields empty to keep current values.\n"
        const val PROMPT_TITLE = "Enter new title (current: %s):"
        const val PROMPT_DESCRIPTION = "Enter new description (current: %s):"
        const val AVAILABLE_STATES = "Available States:"
        const val SELECT_STATE_INDEX = "Select state index (current: %s):"
        const val PROMPT_ASSIGN_USER = "Enter username to assign task to (current: %s ):"
        const val USER_NOT_FOUND = "User not found. Keeping existing assignment."
        const val UNKNOWN = "Unknown"
    }
}