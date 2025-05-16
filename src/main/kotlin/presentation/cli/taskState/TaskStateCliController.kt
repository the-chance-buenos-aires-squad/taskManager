package presentation.cli.taskState

import presentation.UiController
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.helper.ProjectCliHelper.Companion.INVALID_INPUT_MESSAGE

class TaskStateCliController(
    private val projectCliHelper: ProjectCliHelper,
    private val createTaskStateCli: CreateTaskStateCli,
    private val editTaskStateCli: EditTaskStateCli,
    private val deleteTaskStateCli: DeleteTaskStateCli,
    private val getAllTaskStatesCli: GetAllTaskStatesCli,
    private val uiController: UiController
) {
    suspend fun start() {
        while (true) {
            uiController.printMessage(HEADER_MESSAGE_TASK_STATE)

            val projects = projectCliHelper.getProjects()
            if (projects.isEmpty()) {
                uiController.printMessage(NO_PROJECTS)
                return
            }

            val selectedProject = projectCliHelper.selectProject(projects)
            if (selectedProject == null) {
                uiController.printMessage(INVALID_PROJECT)
                return
            }

            uiController.printMessage(DISPLAY_OPTION_MANAGE_TASK)

            when (uiController.readInput().toIntOrNull()) {
                1 -> {
                    createTaskStateCli.createTaskState(selectedProject.id)
                    break
                }

                2 -> {
                    editTaskStateCli.editTaskState(selectedProject.id)
                    break
                }

                3 -> deleteTaskStateCli.deleteTaskState(selectedProject.id)
                4 -> getAllTaskStatesCli.getAllTaskStates(selectedProject.id)
                5 -> return
                null -> uiController.printMessage(INVALID_INPUT_MESSAGE)
                else -> uiController.printMessage("$INVALID_INPUT_MESSAGE from the menu.")
            }
        }
    }

    companion object {
        private const val HEADER_MESSAGE_TASK_STATE =
            "==============================\n" +
                    "    Task State Management     \n" +
                    "===============================\n"

        private const val DISPLAY_OPTION_MANAGE_TASK =
            "================================\n" +
                    " 1. Create Task State       \n" +
                    " 2. Edit Task State         \n" +
                    " 3. Delete Task State       \n" +
                    " 4. View All Task States    \n" +
                    " 5. Back to Main Menu       \n" +
                    "==============================\n" +
                    "Choose an option (1-5):"
        const val INVALID_PROJECT = "invalid project"
        const val NO_PROJECTS = "No Projects yet, try to create project"
    }
}