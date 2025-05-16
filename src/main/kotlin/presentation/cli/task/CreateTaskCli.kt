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
        uiController.printMessage(CREATE_HEADER)

        uiController.printMessage(TITLE_PROMPT, false)
        var title = uiController.readInput()
        if (title.isEmpty()) {
            uiController.printMessage(TITLE_EMPTY)
            uiController.printMessage(TITLE_PROMPT, false)
            title = uiController.readInput()
        }
        if (title.isEmpty()) {
            uiController.printMessage(
                TITLE_CANCEL, false
            )
            return
        }

        uiController.printMessage(DESCRIPTION_PROMPT, false)
        var description = uiController.readInput()
        if (description.isEmpty()) {
            uiController.printMessage(DESCRIPTION_EMPTY)
            uiController.printMessage(DESCRIPTION_PROMPT, false)
            description = uiController.readInput()
        }
        if (description.isEmpty()) {
            uiController.printMessage(
                DESCRIPTION_CANCEL, false
            )
            return
        }


        uiController.printMessage(CHOOSE_STATE, isInline = false)
        val states = getAllStatesUseCase.execute(projectID)
        states.forEachIndexed { index, taskState ->
            uiController.printMessage("${index + 1} - ${taskState.title}||", isInline = false)
        }

        val chosenState: TaskState?
        uiController.printMessage(STATE_PROMPT, false)
        var stateInput = uiController.readInput().toIntOrNull()
        if (stateInput == null || stateInput !in 1..states.size) {
            uiController.printMessage(STATE_INVALID)
            uiController.printMessage(STATE_PROMPT, false)
            stateInput = uiController.readInput().toIntOrNull()
        }
        if (stateInput == null || stateInput !in 1..states.size) {
            uiController.printMessage(
                STATE_CANCEL, false
            )
            return
        }
        chosenState = states[stateInput - 1]

        var assignedUser: User?
        uiController.printMessage(USER_PROMPT, false)
        var username = uiController.readInput()
        assignedUser = userRepository.getUserByUserName(username)
        if (assignedUser == null) {
            uiController.printMessage(USER_NOT_FOUND)
            uiController.printMessage(USER_PROMPT, false)
            username = uiController.readInput()
            assignedUser = userRepository.getUserByUserName(username)
        }
        if (assignedUser == null) {
            uiController.printMessage(
                USER_CANCEL, false
            )
            return
        }


        val newTaskId = UUID.randomUUID()

        try {
            createTaskUseCase.execute(
                id = newTaskId,
                title = title,
                description = description,
                projectId = projectID,
                stateId = chosenState.id,
                assignedTo = assignedUser.id
            ).let {
                when (it) {
                    true -> {
                        uiController.printMessage(SUCCESS)
                    }

                    false -> {
                        uiController.printMessage(FAILURE)
                    }
                }
            }

        } catch (e: UserNotLoggedInException) {
            uiController.printMessage(EXCEPTION_NOT_LOGGED_IN)
        } catch (e: TaskTitleEmptyException) {
            uiController.printMessage(EXCEPTION_TITLE)
        } catch (e: TaskDescriptionEmptyException) {
            uiController.printMessage(EXCEPTION_DESCRIPTION)
        } catch (e: InvalidProjectIdException) {
            uiController.printMessage(EXCEPTION_PROJECT)
        }
    }
    companion object Messages {
        const val CREATE_HEADER = "------ Create Task ------\n-------------------------"
        const val TITLE_PROMPT = "Title: "
        const val TITLE_EMPTY = "Title cannot be empty. Please try again."
        const val TITLE_CANCEL = "It seams that you do not want to enter a Title. Let us go to past screen"

        const val DESCRIPTION_PROMPT = "Description: "
        const val DESCRIPTION_EMPTY = "Description cannot be empty. Please try again."
        const val DESCRIPTION_CANCEL = "It seams that you do not want to enter a Description. Let us go to past screen"

        const val CHOOSE_STATE = "Choose task state: "
        const val STATE_PROMPT = "\nEnter state number: "
        const val STATE_INVALID = "Invalid state selection. Please try again."
        const val STATE_CANCEL = "It seams that you do not want to enter a valid state number. Let us go to past screen"

        const val USER_PROMPT = "Enter the user to assign the task to: "
        const val USER_NOT_FOUND = "User not found. Please try again."
        const val USER_CANCEL = "It seams that you do not want to enter a valid username. Let us go to past screen"

        const val SUCCESS = "Task created successfully!"
        const val FAILURE = "Task did not created successfully!"

        const val EXCEPTION_NOT_LOGGED_IN = "User not logged in"
        const val EXCEPTION_TITLE = "Not valid task Title"
        const val EXCEPTION_DESCRIPTION = "Not valid task Description"
        const val EXCEPTION_PROJECT = "Not valid Project"
    }
}