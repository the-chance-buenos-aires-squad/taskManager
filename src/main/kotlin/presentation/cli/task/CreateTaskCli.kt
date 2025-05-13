package presentation.cli.task


import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import presentation.exceptions.UserNotLoggedInException
import domain.entities.TaskState
import domain.entities.User
import domain.repositories.UserRepository
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class CreateTaskCli(
    private val createTaskUseCase: CreateTaskUseCase,
    private val getAllStatesUseCase: GetAllTaskStatesUseCase,
    private val userRepository: UserRepository,
    private val uiController: UiController,
) {

    suspend fun create(projectID: UUID) {
        uiController.printMessage("------ Create Task ------")
        uiController.printMessage("-------------------------")

        uiController.printMessage("Title: ", false)
        var title = uiController.readInput()
        if (title.isEmpty()) {
            uiController.printMessage("Title cannot be empty. Please try again.")
            uiController.printMessage("Title: ", false)
            title = uiController.readInput()
        }
        if (title.isEmpty()) {
            uiController.printMessage(
                "It seams that you do not want to enter a Title"
                        + " let us go to past screen", false
            )
            return
        }

        uiController.printMessage("Description: ", false)
        var description = uiController.readInput()
        if (description.isEmpty()) {
            uiController.printMessage("Description cannot be empty. Please try again.")
            uiController.printMessage("Description: ", false)
            description = uiController.readInput()
        }
        if (description.isEmpty()) {
            uiController.printMessage(
                "It seams that you do not want to enter a Description"
                        + " let us go to past screen", false
            )
            return
        }


        uiController.printMessage("Choose task state: ", isInline = false)
        val states = getAllStatesUseCase.execute(projectID)
        states.forEachIndexed { index, taskState ->
            uiController.printMessage("${index + 1} - ${taskState.name}||", isInline = false)
        }

        val chosenState: TaskState?
        uiController.printMessage("\nEnter state number: ", false)
        var stateInput = uiController.readInput().toIntOrNull()
        if (stateInput == null || stateInput !in 1..states.size) {
            uiController.printMessage("Invalid state selection. Please try again.")
            uiController.printMessage("Enter state number: ", false)
            stateInput = uiController.readInput().toIntOrNull()
        }
        if (stateInput == null || stateInput !in 1..states.size) {
            uiController.printMessage(
                "It seams that you do not want to enter a valid state number"
                        + " let us go to past screen", false
            )
            return
        }
        chosenState = states[stateInput - 1]

        var assignedUser: User?
        uiController.printMessage("Enter the user to assign the task to: ", false)
        var username = uiController.readInput()
        assignedUser = userRepository.getUserByUserName(username)
        if (assignedUser == null) {
            uiController.printMessage("User not found. Please try again.")
            uiController.printMessage("Enter the user to assign the task to: ", false)
            username = uiController.readInput()
            assignedUser = userRepository.getUserByUserName(username)
        }
        if (assignedUser == null) {
            uiController.printMessage(
                "It seams that you do not want to enter a valid username"
                        + " let us go to past screen", false
            )
            return
        }


        val newTaskId = UUID.randomUUID()

        try {
            createTaskUseCase.createTask(
                id = newTaskId,
                title = title,
                description = description,
                projectId = projectID,
                stateId = chosenState.id,
                assignedTo = assignedUser.id
            ).let {
                when (it) {
                    true -> {
                        uiController.printMessage("Task created successfully!")
                    }

                    false -> {
                        uiController.printMessage("Task did not created successfully!")
                    }
                }
            }

        } catch (e: UserNotLoggedInException) {
            uiController.printMessage(" user not longed in")
        } catch (e: TaskTitleEmptyException) {
            uiController.printMessage("Not valid task Title")
        } catch (e: TaskDescriptionEmptyException) {
            uiController.printMessage("Not valid task Description")
        } catch (e: InvalidProjectIdException) {
            uiController.printMessage("Not valid Project")
        }
    }
}