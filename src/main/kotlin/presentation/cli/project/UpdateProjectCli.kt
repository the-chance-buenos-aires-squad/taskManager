package presentation.cli.project

import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import presentation.UiController
import java.time.LocalDateTime

class UpdateProjectCli(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val uiController: UiController
) {
    suspend fun update() {
        val projects = getAllProjectsUseCase.execute()
        uiController.printMessage("Select a project to update:")
        projects.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1}. ${project.name} - ${project.description}")
        }
        if (projects.isEmpty()) throw NoProjectsFoundException()

        uiController.printMessage("Enter the number of the project to update:")
        val indexInput = uiController.readInput().trim()

        if (indexInput.isEmpty()) throw UserEnterInvalidValueException("ID can't be empty")
        val index = indexInput.toIntOrNull() ?: throw UserEnterInvalidValueException("Should enter valid value")
        if (index > projects.size || index <= 0) throw UserEnterInvalidValueException("should enter found id")
        val selectedProject = projects[index - 1]

        uiController.printMessage("Enter new name:")
        val name = uiController.readInput()
        if (name.isEmpty()) throw UserEnterInvalidValueException("New name can't be empty")

        uiController.printMessage("Enter project description:")
        val description = uiController.readInput()
        if (description.isEmpty()) throw UserEnterInvalidValueException("description can't be empty")

        val updatedProject = selectedProject.copy(
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )

        val result = updateProjectUseCase.execute(updatedProject)
        uiController.printMessage(if (result) "Project updated." else "project not found.")
    }
}