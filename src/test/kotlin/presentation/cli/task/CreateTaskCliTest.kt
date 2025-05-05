package presentation.cli.task

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.customeExceptions.UserNotLoggedInException
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import domain.usecases.AddAuditUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.DummyTaskData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import java.util.*

class CreateTaskCliTest {
    private lateinit var createTaskCli: CreateTaskCli
    private val createTaskUseCase = mockk<CreateTaskUseCase>()
    private val authRepository = mockk<AuthRepository>()
    private val getAllStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val userRepository = mockk<UserRepository>()
    private val uiController = mockk<UiController>()

    @BeforeEach
    fun setup() {
        createTaskCli = CreateTaskCli(
            createTaskUseCase = createTaskUseCase,
            getAllStatesUseCase = getAllStatesUseCase,
            userRepository = userRepository,
            uiController = uiController,
            project = DummyTaskData.project
        )
    }

    @Test
    fun `should create task successfully when all inputs are valid`() {
        // Given
        every { authRepository.getCurrentUser() } returns DummyTaskData.currentUser
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } returns true
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.validInputs

        // When
        createTaskCli.start()
        // Then
        verify(exactly = 1) {
            createTaskUseCase.createTask(
                id = any(),
                title = "Test Title",
                description = "Test Description",
                projectId = DummyTaskData.project.id,
                stateId = UUID.fromString(DummyTaskData.taskStates[0].id),
                assignedTo = DummyTaskData.assignedUser.id
            )
        }
        verify(exactly = 1) { uiController.printMessage("Task created successfully!") }
    }

    @Test
    fun `should handle empty title input and retry`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.emptyTitleInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } returns true

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("Title cannot be empty. Please try again.") }
        verify(exactly = 1) { uiController.printMessage("It seams that you do not want to enter a Title let us go to past screen", false) }
    }

    @Test
    fun `should handle empty description input and retry`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.emptyDescriptionInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } returns true

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("Description cannot be empty. Please try again.") }
        verify(exactly = 1) { uiController.printMessage("It seams that you do not want to enter a Description let us go to past screen", false) }
    }

    @Test
    fun `should handle invalid state selection and retry`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.invalidStateInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } returns true

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("Invalid state selection. Please try again.") }
        verify(exactly = 1) { uiController.printMessage("It seams that you do not want to enter a valid state number let us go to past screen", false) }
    }



    @Test
    fun `should handle invalid username and retry`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.invalidUserInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("nonexistentUser") } returns null
        every { userRepository.getUserByUserName("anotherNonexistentUser") } returns null
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } returns true

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("User not found. Please try again.") }
        verify(exactly = 1) { uiController.printMessage("It seams that you do not want to enter a valid username let us go to past screen", false) }
    }

    @Test
    fun `should handle UserNotLoggedInException`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.validInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } throws UserNotLoggedInException()

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage(" user not longed in") }
    }

    @Test
    fun `should handle TaskTitleEmptyException`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.validInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } throws TaskTitleEmptyException()

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("Not valid task Title") }
    }

    @Test
    fun `should handle TaskDescriptionEmptyException`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.validInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } throws TaskDescriptionEmptyException()

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("Not valid task Description") }
    }

    @Test
    fun `should handle InvalidProjectIdException`() {
        // Given
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.validInputs
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } throws InvalidProjectIdException()

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("Not valid Project") }
    }
}