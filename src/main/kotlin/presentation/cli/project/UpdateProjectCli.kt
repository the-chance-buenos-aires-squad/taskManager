package presentation.cli.project

import domain.usecases.project.UpdateProjectUseCase
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper
import java.time.LocalDateTime

class UpdateProjectCli(
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val projectCliHelper: ProjectCliHelper,
    private val uiController: UiController
) {
    suspend fun update() {
        val projects = projectCliHelper.getProjects().also {
            if (it.isEmpty()) {
                uiController.printMessage(NO_PROJECTS_FOUND)
                return
            }
        }

        val selectedProject = projectCliHelper.selectProject(projects).also {
            if (it == null) {
                uiController.printMessage(NO_PROJECT_SELECTED)
                return
            }
        }

        uiController.printMessage(ENTER_NEW_NAME)
        val name = uiController.readInput()
        if (name.isEmpty()) {
            uiController.printMessage(NEW_NAME_CANT_EMPTY)
            return
        }

        uiController.printMessage(ENTER_PROJECT_DESCRIPTION)
        val description = uiController.readInput()
        if (description.isEmpty()) {
            uiController.printMessage(DESCRIPTION_CANT_EMPTY)
            return
        }

        val updatedProject = selectedProject!!.copy(
            title = name,
            description = description,
            createdAt = LocalDateTime.now()
        )

        try {
            updateProjectUseCase.execute(updatedProject)
            uiController.printMessage(PROJECT_UPDATED)
        } catch (e: Exception) {
            uiController.printMessage("$ERROR_UPDATING_PROJECT ${e.message}")
        }
    }
    companion object {
        const val ENTER_NEW_NAME = "Enter new name:"
        const val NEW_NAME_CANT_EMPTY = "New name can't be empty"
        const val ENTER_PROJECT_DESCRIPTION = "Enter project description:"
        const val DESCRIPTION_CANT_EMPTY = "Description can't be empty"
        const val PROJECT_UPDATED = "Project updated."
        const val ERROR_UPDATING_PROJECT = "Error updating project:"
        const val NO_PROJECTS_FOUND = "no projects found"
        const val NO_PROJECT_SELECTED = "no project was selected"
    }
}