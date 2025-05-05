package presentation.cli.task

import domain.customeExceptions.NoTasksFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.repositories.UserRepository
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.task.GetTasksUseCase
import domain.usecases.task.UpdateTaskUseCase
import domain.usecases.taskState.EditTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.time.LocalDateTime

class UpdateTaskCli(
    private val getAllTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val uiController: UiController,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val userRepository: UserRepository,
    private val editTaskStateUseCase: EditTaskStateUseCase,
) {
    fun update() {
        val allTasks = getAllTasksUseCase.getTasks()
        val allProjects = getAllProjectsUseCase.execute()
        val allStates = getAllTaskStatesUseCase.execute()
        val allUsers = userRepository.getUsers()

        if (allTasks.isEmpty()) throw NoTasksFoundException()
        uiController.printMessage("Select a task to update:")
        allTasks.forEachIndexed { index, task ->
            val projectName = allProjects.find { it.id == task.projectId }?.name ?: "Unknown Project"
            val stateName = allStates.find { it.id == task.stateId }?.name ?: "Unknown State"
            val assignedToName = allUsers.find { it.id == task.assignedTo }?.username ?: "Unknown User"
            uiController.printMessage(
                "${index + 1}. ${task.title}" +
                        " - ${task.description} -$projectName - $stateName" +
                        " - $assignedToName - ${task.createdAt} -" +
                        " ${task.createdBy} - ${task.updatedAt}"
            )
        }
        if (allTasks.isEmpty()) throw NoTasksFoundException()

        uiController.printMessage("Enter the number of the project to update:")
        val indexInput = uiController.readInput().trim()
        if (indexInput.isEmpty()) throw UserEnterInvalidValueException("ID can't be empty")
        val index = indexInput.toIntOrNull() ?: throw UserEnterInvalidValueException("Should enter valid value")
        if (index > allTasks.size || index <= 0) throw UserEnterInvalidValueException("should enter found id")
        val selectedTask = allTasks[index - 1]

        uiController.printMessage("Enter new title or press enter to keep it:")
        val title = uiController.readInput().ifBlank { selectedTask.title }

        uiController.printMessage("Enter task description or press enter to keep it:")
        val description = uiController.readInput().ifBlank { selectedTask.description }


        uiController.printMessage("Change State name or press enter to keep it:")
        val currentState = allStates.find { it.id == selectedTask.stateId }
        uiController.printMessage(
            "Current task state is ${currentState?.name}." +
                    " Enter new name or press enter to keep it:"
        )

        val newStateName = uiController.readInput().ifBlank { currentState?.name }
        val updatedState = newStateName?.let {
            currentState?.copy(
                name = it,
                projectId = currentState.projectId
            )
        }
        if (updatedState != null) {
            editTaskStateUseCase.execute(updatedState)
        }


        uiController.printMessage("Change State name or press enter to keep it:")
        val currentUserAssigned = allUsers.find { it.id == selectedTask.stateId }
        uiController.printMessage(
            "Current task state is ${currentUserAssigned?.username}." +
                    " Enter new name or press enter to keep it:"
        )
        val newUserName = uiController.readInput().ifBlank { currentUserAssigned?.username }
        val updatedUserAssigned = allUsers.find { it.username == newUserName }
        val updateUser = newUserName?.let { updatedUserName ->
            updatedUserAssigned?.let { updatedUserId ->
                currentUserAssigned?.copy(
                    id = updatedUserId.id,
                    username = updatedUserName,
                    password = updatedUserAssigned.password,
                    role = updatedUserAssigned.role,
                    createdAt = updatedUserAssigned.createdAt
                )
            }
        }
        if (updateUser != null) {
            userRepository.updateUser(updateUser)
        }

        val updatedTask = updateTaskUseCase.updateTask(
            id = selectedTask.id,
            title = title,
            description = description,
            projectId = selectedTask.projectId,
            stateId = selectedTask.id,
            assignedTo = updateUser?.id,
            createdBy = selectedTask.createdBy,
            createdAt = selectedTask.createdAt,
            updatedAt = LocalDateTime.now()
        )
        uiController.printMessage(if (updatedTask) "Project updated." else "project not found.")
    }
}