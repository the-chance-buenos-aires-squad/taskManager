package presentation.cli.task

import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.entities.Project
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import domain.usecases.AddAuditUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class CreateTaskCli(
    private val createTaskUseCase: CreateTaskUseCase,
    private val addAuditUseCase: AddAuditUseCase,
    private val authRepository: AuthRepository,
    private val getAllStatesUseCase: GetAllTaskStatesUseCase,
    private val userRepository: UserRepository,
    private val uiController: UiController,
    private val project: Project
) {

    fun start() {
        uiController.printMessage("------ Create Task ------")
        uiController.printMessage("-------------------------")

        uiController.printMessage("Title: ", false)
        var title = uiController.readInput()
        if (title.isEmpty()) {
            uiController.printMessage("Title cannot be empty. Please try again.")
            uiController.printMessage("Title: ", false)
            title = uiController.readInput()
        }
        if (title.isEmpty()) throw UserEnterInvalidValueException("Title can't be empty")

        uiController.printMessage("Description: ", false)
        var description = uiController.readInput()
        if (description.isEmpty()) {
            uiController.printMessage("Description cannot be empty. Please try again.")
            uiController.printMessage("Description: ", false)
            description = uiController.readInput()
        }
        if (description.isEmpty()) throw UserEnterInvalidValueException("Description can't be empty")

        uiController.printMessage("Choose task state:" + "1- todo 2-in progress 3- done")
        val states = getAllStatesUseCase.execute()
        states.forEachIndexed { index, taskState ->
            uiController.printMessage("${index + 1} - ${taskState.name}")
        }

        val chosenState: domain.entities.TaskState?
        uiController.printMessage("Enter state number: ", false)
        var stateInput = uiController.readInput().toIntOrNull()
        if (stateInput == null || stateInput !in 1..states.size) {
            uiController.printMessage("Invalid state selection. Please try again.")
            uiController.printMessage("Enter state number: ", false)
            stateInput = uiController.readInput().toIntOrNull()
        }
        if (stateInput == null || stateInput !in 1..states.size) {
            throw UserEnterInvalidValueException("Invalid task state selection")
        }
        chosenState = states[stateInput - 1]

        var assignedUser: domain.entities.User?
        uiController.printMessage("Enter the username to assign the task to: ", false)
        var username = uiController.readInput()
        assignedUser = userRepository.getUserByUserName(username)
        if (assignedUser == null) {
            uiController.printMessage("User not found. Please try again.")
            uiController.printMessage("Enter the username to assign the task to: ", false)
            username = uiController.readInput()
            assignedUser = userRepository.getUserByUserName(username)
        }
        if (assignedUser == null) throw UserEnterInvalidValueException("Invalid user assignment")

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
            projectId = project.id,
            createdBy = currentUser.id,
            stateId = UUID.fromString(chosenState.id),
            assignedTo = assignedUser.id
        )

        if (taskCreated) {
            addAuditUseCase.addAudit(
                entityId = newTaskId.toString(),
                action = ActionType.CREATE,
                entityType = EntityType.TASK,
                field = "",
                newValue = "new",
                oldValue = "old",
                userId = currentUser.id.toString()
            )
            uiController.printMessage("Task created successfully!")
        } else {
            uiController.printMessage("Failed to create the task.")
        }
    }
}