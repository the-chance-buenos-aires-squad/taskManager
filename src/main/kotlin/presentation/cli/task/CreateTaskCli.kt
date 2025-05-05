package presentation.cli.task

import domain.entities.ActionType
import domain.entities.EntityType
import domain.repositories.AuthRepository
import domain.usecases.AddAuditUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class CreateTaskCli(
    private val createTaskUseCase: CreateTaskUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addAuditUseCase: AddAuditUseCase,
    private val authRepository: AuthRepository,
    private val getAllStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController,
) {

    fun start() {
        uiController.printMessage("------ Create Task ------")
        uiController.printMessage("-------------------------")

        uiController.printMessage("Title: ", false)
        val title = uiController.readInput()

        uiController.printMessage("Description: ", false)
        val description = uiController.readInput()

        // Choose project
        uiController.printMessage("Choose a project:")
        val projects = getAllProjectsUseCase.execute()
        projects.forEachIndexed { index, project ->
            uiController.printMessage("${index + 1} - ${project.name}")
        }

        val chosenProjectIndex = uiController.readInput().toIntOrNull()
        if (chosenProjectIndex == null || chosenProjectIndex !in 1..projects.size) {
            uiController.printMessage("Invalid project selection.")
            return
        }
        val chosenProject = projects[chosenProjectIndex - 1]

        // Choose task state
        uiController.printMessage("Choose task state:")
        val states = getAllStatesUseCase.execute()
        states.forEachIndexed { index, taskState ->
            uiController.printMessage("${index + 1} - ${taskState.name}")
        }

        val chosenStateIndex = uiController.readInput().toIntOrNull()
        if (chosenStateIndex == null || chosenStateIndex !in 1..states.size) {
            uiController.printMessage("Invalid task state selection.")
            return
        }
        val chosenState = states[chosenStateIndex - 1]

        // Get current user
        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            uiController.printMessage("Error: No authenticated user.")
            return
        }

        val newTaskId = UUID.randomUUID()

        val taskCreated = createTaskUseCase.createTask(
            id = newTaskId,
            title = title,
            description = description,
            createdBy = currentUser.id,
            projectId = chosenProject.id,
            stateId = UUID.fromString(chosenState.id)
        )

        if (taskCreated) {
            addAuditUseCase.addAudit(
                entityId = newTaskId.toString(),
                action = ActionType.CREATE,
                entityType = EntityType.TASK,
                field = null,
                newValue = "",
                oldValue = "",
                userId = currentUser.id.toString()
            )
            uiController.printMessage("Task created successfully!")
        } else {
            uiController.printMessage("Failed to create the task.")
        }
    }
}