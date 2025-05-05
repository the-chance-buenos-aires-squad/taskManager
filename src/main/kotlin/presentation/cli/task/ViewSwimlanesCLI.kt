package presentation.cli.task

import domain.customeExceptions.NoProjectsFoundException
import domain.usecases.GetTasksUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController

class ViewSwimlanesCLI(private val uiController: UiController,
                       private val getProjectsUseCase: GetAllProjectsUseCase,
                       private val getTasksUseCase: GetTasksUseCase,
                       private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase) {

    fun start() {
        val projects = getProjectsUseCase.execute()

        uiController.printMessage(HEADER_MESSAGE)

        if (projects.isEmpty()) throw NoProjectsFoundException()

        uiController.printMessage(" Select a project:")
        projects.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1}. ${project.name} - ${project.description}")
        }

        uiController.printMessage("Enter project ID To Delete:")
        val indexInput = uiController.readInput().trim()

    }


    companion object {
        const val HEADER_MESSAGE = "========================================\n" +
                "              view SwimLanes          \n" +
                "========================================\n"
        const val USERNAME_PROMPT_MESSAGE = "username: "
        const val PASSWORD_PROMPT_MESSAGE = "password: "
        const val CONFIRM_PASSWORD_PROMPT_MESSAGE = "confirm password: "
        const val SUCCESS_MESSAGE = "add new user mate %s successfully"
        const val ERROR_MESSAGE = "Error: %s"
    }
}