package presentation.Cli.projectClasses

import domain.customeExceptions.NoProjectsFoundException
import domain.usecases.project.GetAllProjectsUseCase
import presentation.UiController

class GetAllProjectsCli(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val uiController: UiController
) {
    fun getAll() {
        val projects = getAllProjectsUseCase.execute()

        if (projects.isEmpty()) throw NoProjectsFoundException()

        uiController.printMessage(" all projects found:")
        projects.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1}. ${project.name} - ${project.description}")
        }
    }

}