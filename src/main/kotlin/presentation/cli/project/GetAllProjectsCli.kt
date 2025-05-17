package presentation.cli.project

import data.exceptions.NoProjectsFoundException
import domain.usecases.project.GetAllProjectsUseCase
import presentation.UiController

class GetAllProjectsCli(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val uiController: UiController
) {
    suspend fun getAll() {
        try{
            val projects = getAllProjectsUseCase.execute()

            uiController.printMessage(ALL_PROJECTS_FOUND)
            projects.forEachIndexed { index, project ->
                uiController.printMessage("${index + 1}. ${project.title} - ${project.description}")
            }
        }catch (e: NoProjectsFoundException){
            uiController.printMessage(e.localizedMessage)
        }
    }
    companion object {
        const val ALL_PROJECTS_FOUND = "all projects found"
    }
}