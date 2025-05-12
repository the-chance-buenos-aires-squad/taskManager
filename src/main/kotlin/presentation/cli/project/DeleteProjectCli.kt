package presentation.cli.project

import domain.usecases.project.DeleteProjectUseCase
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper

class DeleteProjectCli(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val projectCliHelper: ProjectCliHelper,
    private val uiController: UiController
) {
    suspend fun delete() {
        val projects = projectCliHelper.getProjects().also {
            if (it.isEmpty()) uiController.printMessage("no projects found")
        }
        val selectedProject = projectCliHelper.selectProject(projects).also {
            if (it == null) {
                uiController.printMessage("no project was selected")
                return
            }
        }
        try {
            deleteProjectUseCase.execute(selectedProject!!.id).also {
                if (it) uiController.printMessage("Project deleted.")
            }
        } catch (e: Exception) {
            uiController.printMessage("error deleting project from:${e.message}")
        }

    }
}