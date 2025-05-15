package presentation.cli.task

import presentation.exceptions.UserNotLoggedInException
import domain.entities.Task
import domain.repositories.UserRepository
import domain.usecases.task.GetAllTasksUseCase
import domain.usecases.task.UpdateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class UpdateTaskCli(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val userRepository: UserRepository,
    private val uiController: UiController
) {

    suspend fun update(projectID: UUID) {
        val allTasks = getAllTasksUseCase.execute()
        val tasks = TaskCliUtils.fetchProjectTasks(allTasks, projectID, uiController)
        if (tasks.isEmpty()) return

        val selectedTask = TaskCliUtils.selectTask(tasks, uiController) ?: return

        val statesForProject = getAllTaskStatesUseCase.execute(projectID)
        // val statesForProject = allStates.filter { it.projectId == projectID }

        val updatedTask = TaskCliUtils.promptForUpdatedTask(
            selectedTask,
            statesForProject,
            userRepository,
            uiController
        )


        handleTaskUpdating(updatedTask)
    }

    private suspend fun handleTaskUpdating(updatedTask: Task) {
        try {
            updateTaskUseCase.execute(
                updatedTask.id,
                updatedTask.title,
                updatedTask.description,
                updatedTask.projectId,
                updatedTask.stateId,
                updatedTask.assignedTo,
            ).also { result ->
                when (result) {
                    true -> uiController.printMessage(SUCCESS)
                    false -> uiController.printMessage(ERROR)
                }
            }

        } catch (e: UserNotLoggedInException) {
            uiController.printMessage(e.message ?: "")
        }
    }
    companion object Messages {
        const val SUCCESS = "Task updated successfully."
        const val ERROR = "Failed to update task."
    }
}