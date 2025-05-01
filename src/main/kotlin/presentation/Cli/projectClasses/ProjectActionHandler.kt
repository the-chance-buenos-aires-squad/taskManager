package presentation.Cli.projectClasses

import data.dataSource.util.CheckValidationInputs
import domain.entities.Project
import domain.usecases.CreateProjectUseCase
import domain.usecases.DeleteProjectUseCase
import domain.usecases.UpdateProjectUseCase
import presentation.UiController
import java.time.LocalDateTime

class ProjectActionHandler(
    private val createProjectUseCase: CreateProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val uiController: UiController,
    private val checkValidationInputs: CheckValidationInputs
) {
    fun create() {
        uiController.printMessage("Enter project ID:")
        val id = uiController.readInput().trim()
        checkValidationInputs.checkValidation(id,uiController)
        uiController.printMessage("Enter project name:")
        val name = uiController.readInput()
        checkValidationInputs.checkValidation(name,uiController)
        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        checkValidationInputs.checkValidation(description,uiController)
        val project = Project(
            id = id,
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        val result = createProjectUseCase.execute(project)
        println(if (result) "Project created." else "Failed to create project.")
    }

    fun edit() {
        uiController.printMessage("Enter project ID:")
        val id = uiController.readInput().trim()
        checkValidationInputs.checkValidation(id,uiController)
        uiController.printMessage("Enter new name:")
        val name = uiController.readInput()
        checkValidationInputs.checkValidation(id,uiController)
        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        checkValidationInputs.checkValidation(id,uiController)
        val updatedProject = Project(
            id = id,
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        val result = updateProjectUseCase.execute(updatedProject)
        println(if (result) "Project updated." else "Failed to update project.")
    }

    fun delete() {
        uiController.printMessage("Enter project ID To Delete:")
        val projectId = uiController.readInput().trim()
        val result = deleteProjectUseCase.execute(projectId)
        println(if (result) "Project deleted." else "Failed to delete project.")
    }
}