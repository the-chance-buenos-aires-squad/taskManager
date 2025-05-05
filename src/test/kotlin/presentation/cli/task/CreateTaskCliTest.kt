package presentation.cli.task

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.*
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
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import java.util.*
import kotlin.test.Test

class CreateTaskCliTest {
    private lateinit var createTaskCli: CreateTaskCli
    private val createTaskUseCase = mockk<CreateTaskUseCase>()
    private val addAuditUseCase = mockk<AddAuditUseCase>()
    private val authRepository = mockk<AuthRepository>()
    private val getAllStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val userRepository = mockk<UserRepository>()
    private val uiController = mockk<UiController>()

    @BeforeEach
    fun setup() {
        createTaskCli = CreateTaskCli(
            createTaskUseCase = createTaskUseCase,
            addAuditUseCase = addAuditUseCase,
            authRepository = authRepository,
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
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any(), any()) } returns true
        every { addAuditUseCase.addAudit(any(), any(), any(), any(), any(), any(), any()) } returns DummyTaskData.createDummyAudit()
        every { uiController.printMessage(any()) } returns Unit
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
                createdBy = DummyTaskData.currentUser.id,
                stateId = UUID.fromString(DummyTaskData.taskStates[0].id),
                assignedTo = DummyTaskData.assignedUser.id
            )
        }
        verify(exactly = 1) {
            addAuditUseCase.addAudit(
                entityId = any(),
                action = ActionType.CREATE,
                entityType = EntityType.TASK,
                field = "",
                newValue = "new",
                oldValue = "old",
                userId = DummyTaskData.currentUser.id.toString()
            )
        }
    }

    @Test
    fun `should throw UserEnterInvalidValueException when title is empty after retry`() {
        // Given
        every { uiController.printMessage(any()) } returns Unit
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.emptyTitleInputs

        // When & Then
        val exception = assertThrows<UserEnterInvalidValueException> {
            createTaskCli.start()
        }
        assertThat(exception.message).isEqualTo("Title can't be empty")
        verify { uiController.printMessage("Title cannot be empty. Please try again.") }
    }

    @Test
    fun `should throw UserEnterInvalidValueException when description is empty after retry`() {
        // Given
        every { uiController.printMessage(any()) } returns Unit
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.emptyDescriptionInputs

        // When & Then
        val exception = assertThrows<UserEnterInvalidValueException> {
            createTaskCli.start()
        }
        assertThat(exception.message).isEqualTo("Description can't be empty")
        verify { uiController.printMessage("Description cannot be empty. Please try again.") }
    }

    @Test
    fun `should throw UserEnterInvalidValueException when state selection is invalid after retry`() {
        // Given
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { uiController.printMessage(any()) } returns Unit
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.invalidStateInputs

        // When & Then
        val exception = assertThrows<UserEnterInvalidValueException> {
            createTaskCli.start()
        }
        assertThat(exception.message).isEqualTo("Invalid task state selection")
        verify { uiController.printMessage("Invalid state selection. Please try again.") }
    }

    @Test
    fun `should throw UserEnterInvalidValueException when assigned user is not found after retry`() {
        // Given
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName(any()) } returns null
        every { uiController.printMessage(any()) } returns Unit
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.invalidUserInputs

        // When & Then
        val exception = assertThrows<UserEnterInvalidValueException> {
            createTaskCli.start()
        }
        assertThat(exception.message).isEqualTo("Invalid user assignment")
        verify { uiController.printMessage("User not found. Please try again.") }
    }

    @Test
    fun `should not create task or audit when task creation fails`() {
        // Given
        every { authRepository.getCurrentUser() } returns DummyTaskData.currentUser
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any(), any()) } returns false
        every { uiController.printMessage(any()) } returns Unit
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.validInputs

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 1) { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any(), any()) }
        verify(exactly = 0) { addAuditUseCase.addAudit(any(), any(), any(), any(), any(), any(), any()) }
        verify { uiController.printMessage("Failed to create the task.") }
    }

    @Test
    fun `should not create task or audit when no user is authenticated`() {
        // Given
        every { authRepository.getCurrentUser() } returns null
        every { getAllStatesUseCase.execute() } returns DummyTaskData.taskStates
        every { userRepository.getUserByUserName("assignedUser") } returns DummyTaskData.assignedUser
        every { uiController.printMessage(any()) } returns Unit
        every { uiController.printMessage(any(), any()) } returns Unit
        every { uiController.readInput() } returnsMany DummyTaskData.validInputs

        // When
        createTaskCli.start()

        // Then
        verify(exactly = 0) { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any(), any()) }
        verify(exactly = 0) { addAuditUseCase.addAudit(any(), any(), any(), any(), any(), any(), any()) }
        verify { uiController.printMessage("Error: No authenticated user.") }
    }
}