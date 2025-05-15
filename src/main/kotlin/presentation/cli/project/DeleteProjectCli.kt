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
            if (it.isEmpty()) uiController.printMessage(NO_PROJECTS_FOUND)
        }
        val selectedProject = projectCliHelper.selectProject(projects).also {
            if (it == null) {
                uiController.printMessage(NO_PROJECT_SELECTED)
                return
            }
        }
        try {
            deleteProjectUseCase.execute(selectedProject!!.id).also {
                if (it) uiController.printMessage(PROJECT_DELETED)
            }
        } catch (e: Exception) {
            uiController.printMessage("$DELETE_PROJECT_FROM ${e.message}")
        }
    }
    companion object {
        const val NO_PROJECTS_FOUND = "no projects found"
        const val NO_PROJECT_SELECTED = "no project was selected"
        const val PROJECT_DELETED = "Project deleted."
        const val DELETE_PROJECT_FROM = "error deleting project from:"
    }
}