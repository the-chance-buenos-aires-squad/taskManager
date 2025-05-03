package presentation.cli.projectClasses

import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
import presentation.UiController

class DeleteProjectCli(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val uiController: UiController
) {
    fun delete() {
        val projects = getAllProjectsUseCase.execute()

        uiController.printMessage(" Select a project to delete:")
        projects.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1}. ${project.name} - ${project.description}")
        }

        if (projects.isEmpty()) throw NoProjectsFoundException()

        uiController.printMessage("Enter project ID To Delete:")
        val indexInput = uiController.readInput().trim()
        val index = indexInput.toIntOrNull() ?: throw UserEnterInvalidValueException("Should enter valid value")
        if (index > projects.size || index <= 0) throw UserEnterInvalidValueException("should enter found id")
        val selectedProject = projects[index - 1]

        val deleted = deleteProjectUseCase.execute(selectedProject.id)
        uiController.printMessage(if (deleted) "Project deleted." else "project ID isn't exist.")
    }
}