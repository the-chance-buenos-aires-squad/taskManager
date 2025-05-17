package presentation.cli.task

import domain.entities.Task
import presentation.UiController
import presentation.cli.task.TaskCliUtils.Messages.INVALID_INDEX
import presentation.cli.task.TaskCliUtils.Messages.NO_TASKS_FOUND
import presentation.cli.task.TaskCliUtils.Messages.PROMPT_TASK_INDEX

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



    private object Messages {
        const val NO_TASKS_FOUND = "No tasks found for this project."
        const val PROMPT_TASK_INDEX = "Please choose task number: "
        const val INVALID_INDEX = "Invalid input. Returning to dashboard."
    }
}