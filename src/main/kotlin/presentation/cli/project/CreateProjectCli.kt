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

        uiController.printMessage(ENTER_PROJECT_NAME)
        val name = uiController.readInput()

        uiController.printMessage(ENTER_PROJECT_DESCRIPTION)
        val description = uiController.readInput()
        val project = Project(
            id = id,
            title = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        try {
            createProjectUseCase.execute(project).let {
                if (it) uiController.printMessage(PROJECT_CREATED)
            }
        } catch (e: UserEnterInvalidValueException) {
            uiController.printMessage(e.message.toString())
        } catch (e: Exception) {
            uiController.printMessage("$FAILED_TO_CREATE_PROJECT ${e.message}")
        }
    }
    companion object {
        const val ENTER_PROJECT_NAME = "Enter project name:"
        const val ENTER_PROJECT_DESCRIPTION = "Enter project description:"
        const val PROJECT_CREATED = "Project created."
        const val FAILED_TO_CREATE_PROJECT = "Failed to create project."
    }
}