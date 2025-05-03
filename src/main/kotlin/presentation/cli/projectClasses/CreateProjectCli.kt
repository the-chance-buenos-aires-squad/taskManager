package presentation.cli.projectClasses

import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.Project
import domain.usecases.project.CreateProjectUseCase
import presentation.UiController
import java.time.LocalDateTime
import java.util.UUID

class CreateProjectCli(
    private val createProjectUseCase: CreateProjectUseCase,
    private val uiController: UiController
) {
    fun create() {
        val id = UUID.randomUUID()

        uiController.printMessage("Enter project name:")
        val name = uiController.readInput()
        if (name.isEmpty()) throw UserEnterInvalidValueException("name can't be empty")

        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        if (description.isEmpty()) throw UserEnterInvalidValueException("description can't be empty")

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