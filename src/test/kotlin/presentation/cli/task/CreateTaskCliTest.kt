package presentation.cli.task

import com.google.common.truth.Truth.assertThat
import domain.entities.*
import domain.repositories.AuthRepository
import domain.usecases.AddAuditUseCase
import domain.usecases.CreateTaskUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import presentation.UiController
import java.time.LocalDateTime
import java.util.*

class CreateTaskCliTest {

    private val createTaskUseCase = mockk<CreateTaskUseCase>()
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>()
    private val addAuditUseCase = mockk<AddAuditUseCase>(relaxed = true)
    private val authRepository = mockk<AuthRepository>()
    private val getAllTaskStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val uiController = mockk<UiController>(relaxed = true)

    private lateinit var cli: CreateTaskCli

    private val testUser = User(
        id = UUID.randomUUID(),
        username = "tester",
        password = "secret",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

    @BeforeEach
    fun setUp() {
        cli = CreateTaskCli(
            createTaskUseCase = createTaskUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            addAuditUseCase = addAuditUseCase,
            authRepository = authRepository,
            getAllStatesUseCase = getAllTaskStatesUseCase,
            uiController = uiController,
        )
    }
    @Disabled
    @Test
    fun `start() should create a task and log audit when inputs are valid`() {
        // Arrange
        val taskTitle = "Test Task"
        val taskDescription = "This is a test description"

        val project = Project(
            id = UUID.randomUUID(),
            name = "Test Project",
            description = "Project description",
            createdAt = LocalDateTime.now()
        )

        val state = TaskState(
            id = UUID.randomUUID().toString(),
            name = "In Progress",
            projectId = project.id.toString()
        )

        val inputSequence = listOf(taskTitle, taskDescription, "1", "1")
        every { uiController.readInput() } returnsMany inputSequence
        every { getAllProjectsUseCase.execute() } returns listOf(project)
        every { getAllTaskStatesUseCase.execute() } returns listOf(state)
        every { authRepository.getCurrentUser() } returns testUser

        // Use `any()` for the flexible matching
        every {
            createTaskUseCase.createTask(
                any(),
                eq(taskTitle),
                eq(taskDescription),
                eq(project.id),
                eq(state.id),
                eq(UUID.fromString(state.id)),
                any() // assignedTo
            )
        } returns true

        // Act
        cli.start()

        // Assert
        verify(exactly = 1) {
            createTaskUseCase.createTask(
                any(),
                eq(taskTitle),
                eq(taskDescription),
                eq(project.id),
                eq(state.id),
                eq(UUID.fromString(state.id)),
                any() // assignedTo
            )
        }

        // Capture and verify the audit action
        val capturedAuditEntityId = slot<String>()
        verify(exactly = 1) {
            addAuditUseCase.addAudit(
                entityId = capture(capturedAuditEntityId),
                action = ActionType.CREATE,
                entityType = EntityType.TASK,
                field = null,
                newValue = "",
                oldValue = "",
                userId = eq(testUser.id.toString()) // Ensure userId is correctly matched
            )
        }

        // Verify the captured task ID matches the one created
        assertThat(capturedAuditEntityId.captured).isNotEmpty()
        assertThat(capturedAuditEntityId.captured).isEqualTo(testUser.id.toString())
    }
}