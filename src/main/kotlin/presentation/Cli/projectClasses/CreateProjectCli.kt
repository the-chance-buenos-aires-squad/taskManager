package presentation.Cli.projectClasses

import domain.customeExceptions.UserEnterEmptyValueException
import domain.entities.Project
import domain.usecases.project.CreateProjectUseCase
import presentation.UiController
import java.time.LocalDateTime

class CreateProjectCli(
    private val createProjectUseCase: CreateProjectUseCase,
    private val uiController: UiController
) {
    fun create() {
        uiController.printMessage("Enter project ID:")
        val id = uiController.readInput().trim()
        if (id.isEmpty()) throw UserEnterEmptyValueException("ID can't be empty")
        uiController.printMessage("Enter project name:")
        val name = uiController.readInput()
        if (name.isEmpty()) throw UserEnterEmptyValueException("name can't be empty")
        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        if (description.isEmpty()) throw UserEnterEmptyValueException("description can't be empty")
        val project = Project(
            id = id,
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        val result = createProjectUseCase.execute(project)
        uiController.printMessage(if (result) "Project created." else "Failed to create project.")
    }
}