package presentation.Cli.projectClasses

import domain.customeExceptions.UserEnterEmptyValueException
import domain.entities.Project
import domain.usecases.project.UpdateProjectUseCase
import presentation.UiController
import java.time.LocalDateTime

class UpdateProjectCli(
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val uiController: UiController
) {
    fun update() {
        uiController.printMessage("Enter project ID:")
        val id = uiController.readInput().trim()
        if (id.isEmpty()) throw UserEnterEmptyValueException("New ID can't be empty")
        uiController.printMessage("Enter new name:")
        val name = uiController.readInput()
        if (name.isEmpty()) throw UserEnterEmptyValueException("New name can't be empty")
        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        if (description.isEmpty()) throw UserEnterEmptyValueException("description can't be empty")
        val updatedProject = Project(
            id = id,
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        val result = updateProjectUseCase.execute(updatedProject)
        uiController.printMessage(if (result) "Project updated." else "project not found.")
    }
}