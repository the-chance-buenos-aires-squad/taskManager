package presentation.cli.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.customeExceptions.UserNotLoggedInException
import domain.entities.TaskState
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.DummyTaskData
import dummyData.DummyUser
import dummyData.dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import dummyData.createDummyProject
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import java.time.LocalDateTime
import java.util.*

class CreateTaskCliTest {
    private lateinit var createTaskCli: CreateTaskCli
    private val createTaskUseCase = mockk<CreateTaskUseCase>()
    private val authRepository = mockk<AuthRepository>()
    private val getAllStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val userRepository = mockk<UserRepository>()
    private val uiController = mockk<UiController>(relaxed = true)


    private val dummyStateList: List<TaskState> = listOf(DummyTaskState.todo,DummyTaskState.inProgress,DummyTaskState.done)
    private val dummyProjectID = UUID.randomUUID()
    private val testUser = User(
        id = UUID.randomUUID(),
        username = "tester",
        password = "secret",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )
    val dummyProject = createDummyProject(name = "Test Project", description = "Test Description")


    @BeforeEach
    fun setup() {
        createTaskCli = CreateTaskCli(
            createTaskUseCase = createTaskUseCase,
            getAllStatesUseCase = getAllStatesUseCase,
            userRepository = userRepository,
            uiController = uiController,
        )
    }




    @Test
    fun `should create task successfully with valid inputs`() {
        // Given
        val dummyProjectID = UUID.randomUUID()
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(id = UUID.randomUUID(), name = "To Do", projectId = dummyProjectID)

        every { uiController.readInput() } returnsMany listOf("title", "description", "1", "username")
        every { getAllStatesUseCase.execute() } returns listOf(dummyTaskState)
        every { userRepository.getUserByUserName("username") } returns dummyUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } returns true

        // When
        createTaskCli.start(dummyProjectID)

        // Then
        verify { uiController.printMessage("Task created successfully!") }
    }



    @Test
    fun `should return early when both title inputs are empty`() {
        // Arrange
        val mockCreateTaskUseCase = mockk<CreateTaskUseCase>(relaxed = true)
        val mockGetAllStatesUseCase = mockk<GetAllTaskStatesUseCase>()
        val mockUserRepository = mockk<UserRepository>()
        val mockUiController = mockk<UiController>(relaxed = true)

        val createTaskCli = CreateTaskCli(
            createTaskUseCase = mockCreateTaskUseCase,
            getAllStatesUseCase = mockGetAllStatesUseCase,
            userRepository = mockUserRepository,
            uiController = mockUiController
        )

        every { mockUiController.readInput() } returnsMany listOf(
            "",  // first title input (empty)
            ""   // second title input (still empty)
        )

        val projectId = UUID.randomUUID()

        // Act
        createTaskCli.start(projectId)

        // Assert
        verifySequence {
            mockUiController.printMessage("------ Create Task ------")
            mockUiController.printMessage("-------------------------")
            mockUiController.printMessage("Title: ", false)
            mockUiController.readInput()
            mockUiController.printMessage("Title cannot be empty. Please try again.")
            mockUiController.printMessage("Title: ", false)
            mockUiController.readInput()
            mockUiController.printMessage(
                "It seams that you do not want to enter a Title" +
                        " let us go to past screen", false
            )
        }

        // Make sure task creation logic was never reached
        verify(exactly = 0) { mockGetAllStatesUseCase.execute() }
        verify(exactly = 0) { mockCreateTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) }
    }



    @Test
    fun `should return early when both description inputs are empty`() {
        // Arrange
        val mockCreateTaskUseCase = mockk<CreateTaskUseCase>(relaxed = true)
        val mockGetAllStatesUseCase = mockk<GetAllTaskStatesUseCase>()
        val mockUserRepository = mockk<UserRepository>()
        val mockUiController = mockk<UiController>(relaxed = true)

        val createTaskCli = CreateTaskCli(
            createTaskUseCase = mockCreateTaskUseCase,
            getAllStatesUseCase = mockGetAllStatesUseCase,
            userRepository = mockUserRepository,
            uiController = mockUiController
        )

        every { mockUiController.readInput() } returnsMany listOf(
            "Valid Title",  // Title input
            "",             // First description input (empty)
            ""              // Second description input (still empty)
        )

        val projectId = UUID.randomUUID()

        // Act
        createTaskCli.start(projectId)

        // Assert
        verifySequence {
            mockUiController.printMessage("------ Create Task ------")
            mockUiController.printMessage("-------------------------")
            mockUiController.printMessage("Title: ", false)
            mockUiController.readInput()
            mockUiController.printMessage("Description: ", false)
            mockUiController.readInput()
            mockUiController.printMessage("Description cannot be empty. Please try again.")
            mockUiController.printMessage("Description: ", false)
            mockUiController.readInput()
            mockUiController.printMessage(
                "It seams that you do not want to enter a Description" +
                        " let us go to past screen", false
            )
        }

        // Ensure it exited before going to task state selection
        verify(exactly = 0) { mockGetAllStatesUseCase.execute() }
        verify(exactly = 0) { mockCreateTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should catch UserNotLoggedInException and print error`() {
        // Arrange
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(
            id = UUID.randomUUID(),
            name = "To Do",
            projectId = dummyProjectID // Ensure it matches the project ID
        )
        val dummyStateList = listOf(dummyTaskState) // List with the valid state

        every { uiController.readInput() } returnsMany listOf(
            "Valid Title",           // title
            "Valid Description",     // description
            "1",                     // task state number
            DummyUser.dummyUserOne.username // assigned user
        )
        every { getAllStatesUseCase.execute() } returns dummyStateList
        every { userRepository.getUserByUserName(DummyUser.dummyUserOne.username) } returns dummyUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } throws UserNotLoggedInException()

        // Act
        createTaskCli.start(dummyProjectID)

        // Assert
        verify { uiController.printMessage(" user not longed in", false) }
    }

    @Test
    fun `should catch TaskTitleEmptyException and print error`() {
        // Arrange
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(
            id = UUID.randomUUID(),
            name = "To Do",
            projectId = dummyProjectID
        )
        val dummyStateList = listOf(dummyTaskState)

        every { uiController.readInput() } returnsMany listOf(
            "",  // Empty Title to trigger exception
            "Valid Title",
            "Valid Description",
            "1",
            DummyUser.dummyUserOne.username
        )
        every { getAllStatesUseCase.execute() } returns dummyStateList
        every { userRepository.getUserByUserName(DummyUser.dummyUserOne.username) } returns dummyUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } throws TaskTitleEmptyException()

        // Act
        createTaskCli.start(dummyProjectID)

        // Assert
        verify { uiController.printMessage("Not valid task Title", false) }
    }


    @Test
    fun `should catch InvalidProjectIdException and print error`() {
        // Arrange
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(
            id = UUID.randomUUID(),
            name = "To Do",
            projectId = dummyProjectID
        )
        val dummyStateList = listOf(dummyTaskState)

        every { uiController.readInput() } returnsMany listOf(
            "Valid Title",
            "Valid Description",
            "1",
            DummyUser.dummyUserOne.username
        )
        every { getAllStatesUseCase.execute() } returns dummyStateList
        every { userRepository.getUserByUserName(DummyUser.dummyUserOne.username) } returns dummyUser
        every { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } throws InvalidProjectIdException()

        // Act
        createTaskCli.start(dummyProjectID)

        // Assert
        verify { uiController.printMessage("Not valid Project", false) }
    }

}