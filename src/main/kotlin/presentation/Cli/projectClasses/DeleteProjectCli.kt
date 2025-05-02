package presentation.Cli.projectClasses

import domain.usecases.project.DeleteProjectUseCase
import presentation.UiController

class DeleteProjectCli(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val uiController: UiController
) {
    fun delete() {
        uiController.printMessage("Enter project ID To Delete:")
        val projectId = uiController.readInput().trim()
        val result = deleteProjectUseCase.execute(projectId)
        uiController.printMessage(if (result) "Project deleted." else "project ID isn't exist.")
    }
}