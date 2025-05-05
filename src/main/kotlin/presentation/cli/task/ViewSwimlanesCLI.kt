package presentation.cli.task

import domain.customeExceptions.NoProjectsFoundException
import domain.entities.Project
import domain.entities.TaskStateWithTasks
import domain.usecases.GetTasksGroupedByStateUseCase
import domain.usecases.project.GetAllProjectsUseCase
import presentation.UiController

class ViewSwimlanesCLI(
    private val uiController: UiController,
    private val getProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksGroupedByStateUseCase: GetTasksGroupedByStateUseCase
) {

    fun start() {
        try {
            val projects = getProjectsUseCase.execute()

            uiController.printMessage(HEADER_MESSAGE)

            if (projects.isEmpty()) {
                uiController.printMessage("No projects available.")
                return
            }

            uiController.printMessage("Select a project:")
            projects.forEachIndexed { index, project ->
                uiController.printMessage("${index + 1}. ${project.name} - ${project.description}")
            }

            val selectedProject = getSelectedProject(projects)

            val swimlanes = getTasksGroupedByStateUseCase.getTasksGroupedByState(selectedProject)

            displaySwimlanes(swimlanes)

        } catch (ex: NoProjectsFoundException) {
            uiController.printMessage("Error: ${ex.message}")
        } catch (ex: Exception) {
            uiController.printMessage("An unexpected error occurred: ${ex.message}")
        }
    }

    private fun getSelectedProject(projects: List<Project>): Project {
        while (true) {
            try {
                uiController.printMessage("Enter project number: ")
                val input = uiController.readInput().trim()
                val selectedIndex = input.toInt() - 1

                if (selectedIndex in projects.indices) {
                    return projects[selectedIndex]
                } else {
                    uiController.printMessage("Invalid number. Please enter a number between 1 and ${projects.size}")
                }
            } catch (e: NumberFormatException) {
                uiController.printMessage("Invalid input. Please enter a valid number.")
            }
        }
    }

    private fun displaySwimlanes(swimlanes: List<TaskStateWithTasks>) {
        uiController.printMessage("\nTasks by State:")
        swimlanes.forEach { group ->
            uiController.printMessage("\n▬▬▬ ${group.state.name.toUpperCase()} ▬▬▬")
            if (group.tasks.isEmpty()) {
                uiController.printMessage("  No tasks in this state")
            } else {
                group.tasks.forEachIndexed { index, task ->
                    uiController.printMessage("  ${index + 1}. ${task.title}")
                    uiController.printMessage("     Description: ${task.description}")
                    uiController.printMessage("     Assigned to: ${task.assignedTo ?: "Unassigned"}")
                    uiController.printMessage("     Created at: ${task.createdAt}\n")
                }
            }
        }
        uiController.printMessage("\n${swimlanes.sumOf { it.tasks.size }} total tasks found")
    }

    companion object {
        const val HEADER_MESSAGE = """
            ========================================
                     TASK SWIMLANES VIEW          
            ========================================
        """
    }
}