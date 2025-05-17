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

        uiController.printMessage(FIRST_TITLE_PROMPT_MESSAGE)
        val title = handleTitleInput()?:return

        uiController.printMessage(FIRST_DESCRIPTION_PROMPT_MESSAGE)
        val description = handleDescriptionInput()?:return

        val project = Project(
            id = id,
            title = title,
            description = description,
            createdAt = LocalDateTime.now()
        )
        try {
            createProjectUseCase.execute(project).let {
                if (it) uiController.printMessage(PROJECT_CREATED_MESSAGE)
            }
        } catch (e: Exception) {
            uiController.printMessage("Failed to create project.${e.message}")
        }
    }

    private fun handleTitleInput(): String? {
        val firstInput = uiController.readInput()
        if (firstInput.isNotBlank()) return firstInput

        uiController.printMessage(TITLE_EMPTY_WARNING_MESSAGE, isInline = true)

        val secondInput = uiController.readInput()
        return if (secondInput.isNotBlank()) secondInput else null
    }

    private fun handleDescriptionInput(): String? {
        val firstInput = uiController.readInput()
        if (firstInput.isNotBlank()) return firstInput

        uiController.printMessage(DESCRIPTION_EMPTY_WARNING_MESSAGE, isInline = true)

        val secondInput = uiController.readInput()
        return if (secondInput.isNotBlank()) secondInput else null
    }
    companion object{
        const val FIRST_TITLE_PROMPT_MESSAGE = "Enter project title:"
        const val FIRST_DESCRIPTION_PROMPT_MESSAGE = "Enter project description:"
        const val TITLE_EMPTY_WARNING_MESSAGE = "title cannot be empty please enter title"
        const val DESCRIPTION_EMPTY_WARNING_MESSAGE = "description cannot be empty please enter description"
        const val PROJECT_CREATED_MESSAGE = "Project created."
    }
}