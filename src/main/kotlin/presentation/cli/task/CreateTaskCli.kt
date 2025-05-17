package presentation.cli.task


import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.usecases.task.AddTaskUseCase
import presentation.UiController
import presentation.cli.helper.TaskStateCliHelper
import presentation.cli.taskState.TaskStateCliController.Companion.INVALID_PROJECT
import presentation.cli.taskState.TaskStateCliController.Companion.NO_PROJECTS
import java.util.*

class CreateTaskCli(
    private val addTaskUseCase: AddTaskUseCase,
    private val taskCliHelper: TaskStateCliHelper,
    private val uiController: UiController,
) {

    //TODO re write every thing
    suspend fun addTask(projectID: UUID) {
        uiController.printMessage(
            "------ Create Task ------\n"
                    + "-------------------------\b"
        )

        uiController.printMessage("Title: ", false)
        val title = uiController.readInput()

        if (title.trim().isEmpty()) {
            uiController.printMessage("Title cannot be empty. ")
            return
        }

        uiController.printMessage("Description: ", false)
        val description = uiController.readInput()

        if (description.isEmpty()) {
            uiController.printMessage("Description cannot be empty. Please try again.")
            return
        }

        uiController.printMessage("Choose task state: ", isInline = false)

        val taskStates = taskCliHelper.getTaskStates(projectID).also { projects ->
            if (projects.isEmpty()) {
                uiController.printMessage(NO_PROJECTS)
                return
            }
        }
        val selectedState = taskCliHelper.selectTaskState(taskStates).also {
            if (it == null) {
                uiController.printMessage(INVALID_PROJECT)
                return
            }
        }

        //todo we need get all user to choose user better

        uiController.printMessage("Enter the user to assign the task to: ", false)
        val assignUser = uiController.readInput()

        try{
            addTaskUseCase.execute(
                title = title,
                description = description,
                projectId = projectID,
                stateId = selectedState!!.id,
                assignedTo = assignUser
            )
            uiController.printMessage("add Task Successful")

        }catch (e: Exception){
            uiController.printMessage(e.localizedMessage)
            return
        }

    }

    private fun validateTaskTitle(title: String) {
        if (title.trim().isEmpty()) {
            //todo move to cli
            throw TaskTitleEmptyException()
        }
    }

    private fun validateTaskDescription(description: String) {
        if (description.trim().isEmpty()) {
            //todo move to cli
            throw TaskDescriptionEmptyException()
        }
    }

    private fun validateProjectId(projectId: UUID) {
        if (projectId == UUID(0, 0)) {
            //todo move to cli
            throw InvalidProjectIdException()
        }
    }
}