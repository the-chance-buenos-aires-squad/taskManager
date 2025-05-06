package presentation.cli.TaskState

import presentation.UiController
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.helper.ProjectCliHelper.Companion.EMPTY_INPUT_MESSAGE
import presentation.cli.helper.ProjectCliHelper.Companion.INVALID_INPUT_MESSAGE

class TaskStateCliController(
    private val projectCliHelper: ProjectCliHelper,
    private val createTaskStateCli: CreateTaskStateCli,
    private val editTaskStateCli: EditTaskStateCli,
    private val deleteTaskStateCli: DeleteTaskStateCli,
    private val getAllTaskStatesCli: GetAllTaskStatesCli,
    private val uiController: UiController
) {
    fun start() {
        while (true) {
            uiController.printMessage(HEADER_MESSAGE_TASK_STATE)

            val projects = projectCliHelper.getProjects()
            if (projects.isEmpty()){
                uiController.printMessage("No Projects yet, try to create project")
                return
            }

            val selectedProject = projectCliHelper.selectProject(projects)
            if (selectedProject == null){
                uiController.printMessage("invalid project")
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
                4 -> getAllTaskStatesCli.getAllTaskStates()
                5 -> return
                null -> uiController.printMessage(EMPTY_INPUT_MESSAGE)
                else -> uiController.printMessage(INVALID_INPUT_MESSAGE)
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
                    "==============================\n"
    }
}