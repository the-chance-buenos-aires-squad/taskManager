package presentation.cli.task

import domain.customeExceptions.NoProjectsFoundException
import domain.entities.TaskStateWithTasks
import domain.usecases.GetTasksGroupedByStateUseCase
import presentation.UiController
import presentation.cli.TaskState.DeleteTaskStateCli
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.helper.ProjectCliHelper.Companion.EMPTY_INPUT_MESSAGE
import presentation.cli.helper.ProjectCliHelper.Companion.INVALID_INPUT_MESSAGE

class ViewSwimlanesCLI(
    private val uiController: UiController,
    private val projectCliHelper: ProjectCliHelper,
    private val getTasksGroupedByStateUseCase: GetTasksGroupedByStateUseCase,
    private val createTaskCli: CreateTaskCli
) {

    fun start() {
        while (true){
            try {
                uiController.printMessage(HEADER_MESSAGE)

                val projects = projectCliHelper.getProjects()
                val selectedProject = projectCliHelper.selectProject(projects)

                if (selectedProject == null){
                    uiController.printMessage("invalid project")
                    return
                }
                val swimlanes = getTasksGroupedByStateUseCase.getTasksGroupedByState(selectedProject)

                displaySwimlanes(swimlanes)

                uiController.printMessage(DISPLAY_OPTION_MANAGE_TASK)

                when (uiController.readInput().toIntOrNull()) {
                    1 -> {
                        createTaskCli.create(selectedProject.id)
                    }
                    2 -> {
                        uiController.printMessage("update task cli")
                    }
                    3 -> uiController.printMessage("delete task cli")
                    4 -> return
                    null -> uiController.printMessage(EMPTY_INPUT_MESSAGE)
                    else -> uiController.printMessage(INVALID_INPUT_MESSAGE)
                }

            } catch (ex: NoProjectsFoundException) {
                uiController.printMessage(NO_PROJECT_ERROR_MESSAGE.format(ex.message))
            } catch (ex: Exception) {
                uiController.printMessage(EXCEPTION_ERROR_MESSAGE.format(ex.message))
            }
        }
    }

    private fun displaySwimlanes(swimlanes: List<TaskStateWithTasks>) {
        uiController.printMessage(TASKS_BY_STATE_MESSAGE)
        swimlanes.forEach { taskState ->
            uiController.printMessage(TITLE_STATE_MESSAGE.format(taskState.state.name))
            if (taskState.tasks.isEmpty()) {
                uiController.printMessage(NO_TASKS_IN_STATE_MESSAGE)
            } else {
                taskState.tasks.forEachIndexed { index, task ->
                    val formattedMessage = TASK_FORMAT.format(
                        index + 1,
                        task.title,
                        task.description,
                        task.assignedTo ?: "Unassigned",
                        task.createdAt
                    )
                    uiController.printMessage(formattedMessage)
                }
            }
        }
        uiController.printMessage(TOTAL_TASKS_FOUND_MESSAGE.format(swimlanes.sumOf { it.tasks.size }))
    }

    companion object {
        private const val HEADER_MESSAGE =
            "========================================\n" +
                    "         TASK SWIMLANES VIEW             \n" +
                    "========================================\n"
        private const val TITLE_STATE_MESSAGE = "\n=====( %S )====="
        private const val NO_PROJECT_ERROR_MESSAGE = " Error: %S"
        private const val EXCEPTION_ERROR_MESSAGE = " An unexpected error occurred: %S"
        private const val TOTAL_TASKS_FOUND_MESSAGE = "\n\n %S total tasks found\n"
        private const val TASKS_BY_STATE_MESSAGE = "\nTasks by State:"
        private const val NO_TASKS_IN_STATE_MESSAGE = " No tasks in this state"
        private const val TASK_FORMAT =
            "  %d. %s" + "     Description: %s" + "     Assigned to: %s" + "     Created at: %s \n"

        const val DISPLAY_OPTION_MANAGE_TASK =
            "================================\n" +
                    " 1. Create Task       \n" +
                    " 2. Edit Task         \n" +
                    " 3. Delete Task        \n" +
                    " 4. Back to Main Menu       \n" +
                    "==============================\n"
    }
}