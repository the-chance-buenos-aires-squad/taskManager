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
                uiController.printMessage("No projects found.")
                return
            }
        }

        val selectedProject = projectCliHelper.selectProject(projects).also {
            if (it == null) {
                uiController.printMessage("No project was selected.")
                return
            }
        }

        uiController.printMessage("Enter new name:")
        val name = uiController.readInput()
        if (name.isEmpty()) {
            uiController.printMessage("New name can't be empty")
            return
        }

        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        if (description.isEmpty()) {
            uiController.printMessage("Description can't be empty")
            return
        }

        val updatedProject = selectedProject!!.copy(
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )

        try {
            updateProjectUseCase.execute(updatedProject)
            uiController.printMessage("Project updated.")
        } catch (e: Exception) {
            uiController.printMessage("Error updating project: ${e.message}")
        }
    }
}