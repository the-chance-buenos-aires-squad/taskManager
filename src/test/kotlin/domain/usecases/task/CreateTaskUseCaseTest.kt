package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.customeExceptions.UserNotLoggedInException
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
class CreateTaskUseCaseTest {
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val addAuditUseCase: AddAuditUseCase = mockk(relaxed = true)
    private val validTitle = "Valid Task Title"
    private val validDescription = "Valid Task Description"
    private val validProjectId = UUID.randomUUID()
    private val validStateId = UUID.randomUUID()
    private val validAssignedToId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        createTaskUseCase = CreateTaskUseCase(taskRepository, addAuditUseCase, authRepository)

        // Default mock behavior
        every { taskRepository.addTask(any()) } returns true
    }

    @Test
    fun `should create task successfully when all parameters are valid`() {
        // Given
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        // When
        val result = createTaskUseCase
            .createTask(
                UUID.randomUUID(),
                validTitle,
                validDescription,
                validProjectId,
                validStateId,
                validAssignedToId,
            )

        // Then
        assertThat(result).isTrue()  // Ensure the result is true (task creation succeeded)
        verify(exactly = 1) { taskRepository.addTask(any()) }
    }

    @Test
    fun `should throw TaskTitleEmptyException when title is empty`() {
        assertThrows<TaskTitleEmptyException> {
            createTaskUseCase.createTask(
                UUID.randomUUID(),
                " ", // Empty title
                validDescription,
                validProjectId,
                validStateId,
                validAssignedToId,

            )
        }
    }

    @Test
    fun `should throw TaskDescriptionEmptyException when description is empty`() {
        assertThrows<TaskDescriptionEmptyException> {
            createTaskUseCase.createTask(
                UUID.randomUUID(),
                validTitle,
                "", // Empty description
                validProjectId,
                validStateId,
                validAssignedToId,

            )
        }
    }

    @Test
    fun `should throw InvalidProjectIdException when projectId is zero`() {
        assertThrows<InvalidProjectIdException> {
            createTaskUseCase.createTask(
                UUID.randomUUID(),
                validTitle,
                validDescription,
                UUID(0, 0), // Zero UUID
                validStateId,
                validAssignedToId,

            )
        }
    }

    @Test
    fun `should use provided stateId when stateId is given`() {
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        createTaskUseCase.createTask(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,

        )

        assertThat(taskSlot.captured.stateId).isEqualTo(validStateId)
    }

    @Test
    fun `should use provided assignedTo when assignedTo is given`() {
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        createTaskUseCase.createTask(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
        )

        assertThat(taskSlot.captured.assignedTo).isEqualTo(validAssignedToId)
    }

    @Test
    fun `should set assignedTo to null when assignedTo is not provided`() {
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        createTaskUseCase.createTask(
            id = UUID.randomUUID(),
            title = validTitle,
            description = validDescription,
            projectId = validProjectId,
            stateId = validStateId,
        )

        assertThat(taskSlot.captured.assignedTo).isNull()
    }

//    @Test
//    fun `should set createdBy correctly when creating task`() {
//        val taskSlot = slot<Task>()
//        every { taskRepository.addTask(capture(taskSlot)) } returns true
//
//        createTaskUseCase.createTask(
//            UUID.randomUUID(),
//            validTitle,
//            validDescription,
//            validProjectId,
//            validStateId,
//            validAssignedToId,
//
//        )
//
//        assertThat(taskSlot.captured.createdBy).isEqualTo()
//    }

    @Test
    fun `should return true when task is successfully created`() {
        every { taskRepository.addTask(any()) } returns true  // Mocking the successful creation response as true

        val result = createTaskUseCase.createTask(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
        )

        assertThat(result).isTrue()  // Checking that the result is true, indicating success
    }

    @Test
    fun `should allow title and description with leading or trailing spaces`() {
        val trimmedTitle = "  Valid Title  "
        val trimmedDescription = "  Valid Description  "
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        createTaskUseCase.createTask(
            UUID.randomUUID(),
            trimmedTitle,
            trimmedDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
        )

        verify { taskRepository.addTask(any()) }
        assertThat(taskSlot.captured.title).isEqualTo(trimmedTitle)
        assertThat(taskSlot.captured.description).isEqualTo(trimmedDescription)
    }

    @Test
    fun `should throw TaskTitleEmptyException when title is only whitespace characters`() {
        assertThrows<TaskTitleEmptyException> {
            createTaskUseCase.createTask(
                UUID.randomUUID(),
                "\n\t", // whitespace-only
                validDescription,
                validProjectId,
                validStateId,
                validAssignedToId,
            )
        }
    }

    @Test
    fun `should throw TaskDescriptionEmptyException when description is only whitespace characters`() {
        assertThrows<TaskDescriptionEmptyException> {
            createTaskUseCase.createTask(
                UUID.randomUUID(),
                validTitle,
                "\t\t  ", // whitespace-only
                validProjectId,
                validStateId,
                validAssignedToId,
            )
        }
    }

    @Test
    fun `should accept valid manually created UUID for projectId`() {
        val manualUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        createTaskUseCase.createTask(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            manualUUID,
            validStateId,
            validAssignedToId,

        )

        assertThat(taskSlot.captured.projectId).isEqualTo(manualUUID)
    }


    @Test
    fun `should return true when user is logged in and creation successful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserOne
        every { taskRepository.addTask(any()) } returns true

        //when
        val result = createTaskUseCase.createTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
        )


        //then
        assertThat(result).isTrue()
    }


    @Test
    fun `should return false when user is logged in and creation unSuccessful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserOne
        every { taskRepository.addTask(any()) } returns false

        //when
        val result = createTaskUseCase.createTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
        )


        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should through UserNotLoggedInException when user not logged in`() {
        //given
        every { authRepository.getCurrentUser() } returns null
        //when & then
        assertThrows<UserNotLoggedInException> {
            createTaskUseCase.createTask(
                id = DummyTasks.validTask.id,
                title = DummyTasks.validTask.title,
                description = DummyTasks.validTask.description,
                projectId = DummyTasks.validTask.projectId,
                stateId = DummyTasks.validTask.stateId,
                assignedTo = DummyTasks.validTask.assignedTo,
            )
        }
    }

    @Test
    fun `should add audit when user is logged in and creation is successful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserOne
        every { taskRepository.addTask(any()) } returns true

        //when
        createTaskUseCase.createTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
        )

        //then
        verify { addAuditUseCase.addAudit(any(), any(), any(), any(), any(), any(), any()) }
    }

}