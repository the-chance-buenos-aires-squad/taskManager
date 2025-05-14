package presentation.cli.project

import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.Project
import domain.usecases.project.CreateProjectUseCase
import presentation.UiController
import java.time.LocalDateTime
import java.util.*

class CreateProjectCli(
    private val createProjectUseCase: CreateProjectUseCase,
    private val uiController: UiController
) {
    suspend fun create() {
        val id = UUID.randomUUID()

        uiController.printMessage("Enter project name:")
        val name = uiController.readInput()

        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        val project = Project(
            id = id,
            title = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        try {
            createProjectUseCase.execute(project).let {
                if (it) uiController.printMessage("Project created.")
            }
        } catch (e: UserEnterInvalidValueException) {
            uiController.printMessage(e.message.toString())
        } catch (e: Exception) {
            uiController.printMessage("Failed to create project.${e.message}")
        }
    }
}