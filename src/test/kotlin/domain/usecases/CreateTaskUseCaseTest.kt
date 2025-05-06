package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.entities.Task
import domain.repositories.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CreateTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    private val validTitle = "Valid Task Title"
    private val validDescription = "Valid Task Description"
    private val validProjectId = UUID.randomUUID()
    private val validStateId = UUID.randomUUID()
    private val validAssignedToId = UUID.randomUUID()
    private val validCreatedById = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(taskRepository)

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
                title = validTitle,
                description = validDescription,
                projectId = validProjectId,
                stateId = validStateId,
                createdBy = validCreatedById
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
                validCreatedById
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
                validCreatedById
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
                validCreatedById
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
            validCreatedById
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
            validCreatedById
        )

        assertThat(taskSlot.captured.assignedTo).isEqualTo(validAssignedToId)
    }

    @Test
    fun `should set assignedTo to null when assignedTo is not provided`() {
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        createTaskUseCase.createTask(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            null, // Null assignedTo
            validCreatedById
        )

        assertThat(taskSlot.captured.assignedTo).isNull()
    }

    @Test
    fun `should set createdBy correctly when creating task`() {
        val taskSlot = slot<Task>()
        every { taskRepository.addTask(capture(taskSlot)) } returns true

        createTaskUseCase.createTask(
            UUID.randomUUID(),
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
            validCreatedById
        )

        assertThat(taskSlot.captured.createdBy).isEqualTo(validCreatedById)
    }

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
            validCreatedById
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
            validCreatedById
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
                validCreatedById
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
                validCreatedById
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
            validCreatedById
        )

        assertThat(taskSlot.captured.projectId).isEqualTo(manualUUID)
    }
}