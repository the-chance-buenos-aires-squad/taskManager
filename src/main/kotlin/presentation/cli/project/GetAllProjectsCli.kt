package presentation.cli.project

import domain.usecases.project.GetAllProjectsUseCase
import presentation.UiController

class GetAllProjectsCli(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val uiController: UiController
) {
    suspend fun getAll() {
        val projects = getAllProjectsUseCase.execute()


        uiController.printMessage(" all projects found:")
        projects.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1}. ${project.title} - ${project.description}")
        }
    }

}