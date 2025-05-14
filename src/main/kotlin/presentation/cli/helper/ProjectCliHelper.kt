package presentation.cli.helper

import domain.entities.Project
import domain.usecases.project.GetAllProjectsUseCase
import presentation.UiController

class ProjectCliHelper(
    private val getProjectsUseCase: GetAllProjectsUseCase,
    private val uiController: UiController

) {
    suspend fun getProjects(): List<Project> {
        return try {
            getProjectsUseCase.execute()
        } catch (ex: Exception) {
            uiController.printMessage(EXCEPTION_MESSAGE.format(ex.message))
            emptyList()
        }
    }

    fun selectProject(projects: List<Project>): Project? {
        if (projects.isEmpty()) return null

        uiController.printMessage(SELECT_PROJECT_MESSAGE)
        projects.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1}. ${project.title} - ${project.description}")
        }

        return selectProjectFromList(projects)
    }

    private fun selectProjectFromList(projects: List<Project>): Project {
        while (true) {
            uiController.printMessage(ENTER_PROJECT_MESSAGE, true)
            val input = uiController.readInput().trim()

            when (val selectedIndex = input.toIntOrNull()?.minus(1)) {
                null -> uiController.printMessage(INAVLID_INPUT_MESSAGE)
                else -> {
                    if (selectedIndex in projects.indices) {
                        return projects[selectedIndex]
                    }
                    uiController.printMessage(INVALID_INPUT_MESSAGE)
                }
            }
        }
    }


    companion object {
        const val EXCEPTION_MESSAGE = "An unexpected error occurred: %S"
        const val ENTER_PROJECT_MESSAGE = "Enter project number: "
        const val SELECT_PROJECT_MESSAGE = "Select project number: "
        const val INAVLID_INPUT_MESSAGE = "Invalid input: please enter a valid number."
        const val INVALID_INPUT_MESSAGE = "Invalid Input: please enter a valid number from the menu."
    }

}
