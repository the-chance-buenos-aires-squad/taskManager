package presentation.cli.task

import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskTitleEmptyException
import domain.customeExceptions.UserNotLoggedInException
import domain.entities.TaskState
import domain.repositories.UserRepository
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.DummyUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import java.util.*

class CreateTaskCliTest {
    private lateinit var createTaskCli: CreateTaskCli
    private val createTaskUseCase = mockk<CreateTaskUseCase>()
    private val getAllStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val userRepository = mockk<UserRepository>()
    private val uiController = mockk<UiController>(relaxed = true)

    private val dummyProjectID = UUID.randomUUID()

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
    fun `should create task successfully with valid inputs`() = runTest {
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(id = UUID.randomUUID(), name = "To Do", projectId = dummyProjectID)

        every { uiController.readInput() } returnsMany listOf("title", "description", "1", "username")
        coEvery { getAllStatesUseCase.execute(dummyProjectID) } returns listOf(dummyTaskState)
        coEvery { userRepository.getUserByUserName("username") } returns dummyUser
        coEvery { createTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) } returns true

        createTaskCli.create(dummyProjectID)

        verify { uiController.printMessage("Task created successfully!") }
    }

    @Test
    fun `should return early when both title inputs are empty`() = runTest {
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

        every { mockUiController.readInput() } returnsMany listOf("", "")

        val projectId = UUID.randomUUID()

        createTaskCli.create(projectId)

        verifySequence {
            mockUiController.printMessage("------ Create Task ------")
            mockUiController.printMessage("-------------------------")
            mockUiController.printMessage("Title: ", false)
            mockUiController.readInput()
            mockUiController.printMessage("Title cannot be empty. Please try again.")
            mockUiController.printMessage("Title: ", false)
            mockUiController.readInput()
            mockUiController.printMessage(
                "It seams that you do not want to enter a Title let us go to past screen",
                false
            )
        }

        coVerify(exactly = 0) { mockGetAllStatesUseCase.execute(dummyProjectID) }
        coVerify(exactly = 0) { mockCreateTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should return early when both description inputs are empty`() = runTest {
        val mockCreateTaskUseCase = mockk<CreateTaskUseCase>(relaxed = true)
        val mockGetAllStatesUseCase = mockk<GetAllTaskStatesUseCase>(relaxed = true)
        val mockUserRepository = mockk<UserRepository>(relaxed = true)
        val mockUiController = mockk<UiController>(relaxed = true)

        val createTaskCli = CreateTaskCli(
            createTaskUseCase = mockCreateTaskUseCase,
            getAllStatesUseCase = mockGetAllStatesUseCase,
            userRepository = mockUserRepository,
            uiController = mockUiController
        )

        every { mockUiController.readInput() } returnsMany listOf("Valid Title", "", "")

        val projectId = UUID.randomUUID()

        createTaskCli.create(projectId)

        verifySequence {
            mockUiController.printMessage("------ Create Task ------")
            mockUiController.printMessage("-------------------------")
            mockUiController.printMessage("Title: ", false)
            mockUiController.readInput()
            mockUiController.printMessage("Description: ", false)
            mockUiController.readInput()
            mockUiController.printMessage("Description cannot be empty. Please try again.", false)
            mockUiController.printMessage("Description: ", false)
            mockUiController.readInput()
            mockUiController.printMessage(
                "It seams that you do not want to enter a Description let us go to past screen",
                false
            )
        }

        coVerify(exactly = 0) { mockGetAllStatesUseCase.execute(dummyProjectID) }
        coVerify(exactly = 0) { mockCreateTaskUseCase.createTask(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should catch UserNotLoggedInException and print error`() = runTest {
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(id = UUID.randomUUID(), name = "To Do", projectId = dummyProjectID)
        val dummyStateList = listOf(dummyTaskState)

        every { uiController.readInput() } returnsMany listOf(
            "Valid Title", "Valid Description", "1", dummyUser.username
        )
        coEvery { getAllStatesUseCase.execute(dummyProjectID) } returns dummyStateList
        coEvery { userRepository.getUserByUserName(dummyUser.username) } returns dummyUser
        coEvery {
            createTaskUseCase.createTask(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws UserNotLoggedInException()

        createTaskCli.create(dummyProjectID)

        verify { uiController.printMessage(" user not longed in", false) }
    }

    @Test
    fun `should catch TaskTitleEmptyException and print error`() = runTest {
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(id = UUID.randomUUID(), name = "To Do", projectId = dummyProjectID)
        val dummyStateList = listOf(dummyTaskState)

        every { uiController.readInput() } returnsMany listOf(
            "",
            "Valid Title",
            "Valid Description",
            "1",
            dummyUser.username
        )
        coEvery { getAllStatesUseCase.execute(dummyProjectID) } returns dummyStateList
        coEvery { userRepository.getUserByUserName(dummyUser.username) } returns dummyUser
        coEvery {
            createTaskUseCase.createTask(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws TaskTitleEmptyException()

        createTaskCli.create(dummyProjectID)

        verify { uiController.printMessage("Not valid task Title", false) }
    }

    @Test
    fun `should catch InvalidProjectIdException and print error`() = runTest {
        val dummyUser = DummyUser.dummyUserOne
        val dummyTaskState = TaskState(id = UUID.randomUUID(), name = "To Do", projectId = dummyProjectID)
        val dummyStateList = listOf(dummyTaskState)

        every { uiController.readInput() } returnsMany listOf(
            "Valid Title",
            "Valid Description",
            "1",
            dummyUser.username
        )
        coEvery { getAllStatesUseCase.execute(dummyProjectID) } returns dummyStateList
        coEvery { userRepository.getUserByUserName(dummyUser.username) } returns dummyUser
        coEvery {
            createTaskUseCase.createTask(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws InvalidProjectIdException()

        createTaskCli.create(dummyProjectID)

        verify { uiController.printMessage("Not valid Project", false) }
    }
}