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
        every { taskRepository.createTask(any()) } answers { firstArg() }
    }
    
    @Test
    fun `should create task successfully when all parameters are valid`() {
        // Given
        val taskSlot = slot<Task>()
        every { taskRepository.createTask(capture(taskSlot)) } returns mockk()
        
        // When
        createTaskUseCase.createTask(
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
            validCreatedById
        )
        
        // Then
        verify(exactly = 1) { taskRepository.createTask(any()) }
    }
    
    @Test
    fun `should throw TaskTitleEmptyException when title is empty`() {
        // When & Then
        assertThrows<TaskTitleEmptyException> {
            createTaskUseCase.createTask(
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
        // When & Then
        assertThrows<TaskDescriptionEmptyException> {
            createTaskUseCase.createTask(
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
        // When & Then
        assertThrows<InvalidProjectIdException> {
            createTaskUseCase.createTask(
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
        // Given
        val taskSlot = slot<Task>()
        every { taskRepository.createTask(capture(taskSlot)) } returns mockk()
        
        // When
        createTaskUseCase.createTask(
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
            validCreatedById
        )
        
        // Then
        assertThat(taskSlot.captured.stateId).isEqualTo(validStateId)
    }
    
    @Test
    fun `should generate new stateId when stateId is null`() {
        // Given
        val taskSlot = slot<Task>()
        every { taskRepository.createTask(capture(taskSlot)) } returns mockk()
        
        // When
        createTaskUseCase.createTask(
            validTitle,
            validDescription,
            validProjectId,
            null, // Null stateId
            validAssignedToId,
            validCreatedById
        )
        
        // Then
        assertThat(taskSlot.captured.stateId).isNotNull()
    }
    
    @Test
    fun `should use provided assignedTo when assignedTo is given`() {
        // Given
        val taskSlot = slot<Task>()
        every { taskRepository.createTask(capture(taskSlot)) } returns mockk()
        
        // When
        createTaskUseCase.createTask(
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
            validCreatedById
        )
        
        // Then
        assertThat(taskSlot.captured.assignedTo).isEqualTo(validAssignedToId)
    }
    
    @Test
    fun `should set assignedTo to null when assignedTo is not provided`() {
        // Given
        val taskSlot = slot<Task>()
        every { taskRepository.createTask(capture(taskSlot)) } returns mockk()
        
        // When
        createTaskUseCase.createTask(
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            null, // Null assignedTo
            validCreatedById
        )
        
        // Then
        assertThat(taskSlot.captured.assignedTo).isNull()
    }
    
    @Test
    fun `should set createdBy correctly when creating task`() {
        // Given
        val taskSlot = slot<Task>()
        every { taskRepository.createTask(capture(taskSlot)) } returns mockk()
        
        // When
        createTaskUseCase.createTask(
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
            validCreatedById
        )
        
        // Then
        assertThat(taskSlot.captured.createdBy).isEqualTo(validCreatedById)
    }
    
    @Test
    fun `should return task from repository when creating task`() {
        // Given
        val expectedTask = Task(
            title = validTitle,
            description = validDescription,
            projectId = validProjectId,
            stateId = validStateId,
            assignedTo = validAssignedToId,
            createdBy = validCreatedById
        )
        every { taskRepository.createTask(any()) } returns expectedTask
        
        // When
        val result = createTaskUseCase.createTask(
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId,
            validCreatedById
        )
        
        // Then
        assertThat(result).isEqualTo(expectedTask)
    }
}