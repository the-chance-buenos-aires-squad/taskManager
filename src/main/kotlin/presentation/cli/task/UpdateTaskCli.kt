package presentation.cli.task

import domain.usecases.task.GetAllTasksUseCase
import domain.usecases.task.UpdateTaskUseCase
import presentation.UiController
import presentation.cli.helper.TaskStateCliHelper
import presentation.cli.taskState.TaskStateCliController.Companion.INVALID_PROJECT
import presentation.cli.taskState.TaskStateCliController.Companion.NO_PROJECTS
import java.util.*

class UpdateTaskCli(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val taskCliHelper: TaskStateCliHelper,
    private val uiController: UiController
) {

    suspend fun update(projectID: UUID) {

        uiController.printMessage(HEADER_MESSAGE)

        val allTasks = getAllTasksUseCase.execute()
        val tasks = TaskCliUtils.fetchProjectTasks(allTasks, projectID, uiController)
        if (tasks.isEmpty()) return
        val selectedTask = TaskCliUtils.selectTask(tasks, uiController) ?: return

        uiController.printMessage(NEW_TITLE_MESSAGE, false)
        var newtTitle = uiController.readInput()
        if (newtTitle.trim().isEmpty()) {
            newtTitle = selectedTask.title
        }


        uiController.printMessage(NEW_DESCRIPTION_MESSAGE, false)
        var newDescription = uiController.readInput()
        if (newDescription.trim().isEmpty()) {
            newDescription = selectedTask.description
        }

        val taskStates = taskCliHelper.getTaskStates(projectID).also { projects ->
            if (projects.isEmpty()) {
                uiController.printMessage(NO_PROJECTS)
                return
            }
        }

        val selectedNewState = taskCliHelper.selectTaskState(taskStates).also {
            if (it == null) {
                uiController.printMessage(INVALID_PROJECT)
                return
            }
        }

        uiController.printMessage(NEW_USER_ASSIGN_MESSAGE, false)
        var assignUser = uiController.readInput()
        if (assignUser.trim().isEmpty()) {
            assignUser = selectedTask.assignedTo.toString()
        }

        try {
            updateTaskUseCase.execute(
                id = selectedTask.id,
                title = newtTitle,
                description = newDescription,
                projectId = projectID,
                stateId = selectedNewState!!.id,
                assignedTo = assignUser,
                createdAt = selectedTask.createdAt
            )

            uiController.printMessage(UPDATED_TASK_SUCCESS_MESSAGE)

        } catch (e: Exception) {
            uiController.printMessage(ERROR_MESSAGE.format(e.localizedMessage))
            return
        }

    }


    companion object Messages {
        const val HEADER_MESSAGE = "======== Update Task ======\n" +
                "============================\n"
        const val NEW_TITLE_MESSAGE = "enter title or enter to put same title : "
        const val NEW_DESCRIPTION_MESSAGE = "enter Description or enter to put same title:  "
        const val NEW_USER_ASSIGN_MESSAGE = "Enter the user to assign the task to: "
        const val UPDATED_TASK_SUCCESS_MESSAGE = "Task updated successfully."
        const val ERROR_MESSAGE = "Failed to update task. %S"
    }
}