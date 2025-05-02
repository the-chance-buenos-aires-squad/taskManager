package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.task.TaskDataSource
import data.repositories.mappers.Mapper
import domain.entities.Task
import domain.repositories.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImplTest {

    private val mockTaskDataSource = mockk<TaskDataSource>(relaxed = true)
    private val mockTaskMapper = mockk<Mapper<Task>>(relaxed = true)
    private lateinit var taskRepository: TaskRepository

    @BeforeEach
    fun setUp() {
        taskRepository = TaskRepositoryImpl(mockTaskDataSource,mockTaskMapper)

        every { mockTaskMapper.mapEntityToRow(any()) } returns listOf("mapped-task-data")
        every { mockTaskDataSource.addTask(any()) } returns true
    }

    @Test
    fun `should generate new id when creating a task`() {
        // Given
        val initialId = UUID.randomUUID()
        val initialTask = createSampleTask(id = initialId)

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.id).isEqualTo(initialTask.id)
    }

    @Test
    fun `should set current time as createdAt when creating a task`() {
        // Given
        val pastDateTime = LocalDateTime.now().minusDays(5)
        val initialTask = createSampleTask(createdAt = pastDateTime)

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.createdAt).isEqualTo(initialTask.createdAt)
    }

    @Test
    fun `should set current time as updatedAt when creating a task`() {
        // Given
        val pastDateTime = LocalDateTime.now().minusDays(5)
        val initialTask = createSampleTask(updatedAt = pastDateTime)

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.updatedAt).isEqualTo(initialTask.updatedAt)
    }

    @Test
    fun `should preserve title when creating a task`() {
        // Given
        val initialTask = createSampleTask(title = "Specific Title")

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.title).isEqualTo(initialTask.title)
    }

    @Test
    fun `should preserve description when creating a task`() {
        // Given
        val initialTask = createSampleTask(description = "Specific Description")

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.description).isEqualTo(initialTask.description)
    }

    @Test
    fun `should preserve projectId when creating a task`() {
        // Given
        val initialTask = createSampleTask(projectId = UUID.randomUUID())

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.projectId).isEqualTo(initialTask.projectId)
    }

    @Test
    fun `should preserve stateId when creating a task`() {
        // Given
        val initialTask = createSampleTask(stateId = UUID.randomUUID())

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.stateId).isEqualTo(initialTask.stateId)
    }

    @Test
    fun `should preserve assignedTo when creating a task`() {
        // Given
        val initialTask = createSampleTask(assignedTo = UUID.randomUUID())

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.assignedTo).isEqualTo(initialTask.assignedTo)
    }

    @Test
    fun `should preserve createdBy when creating a task`() {
        // Given
        val initialTask = createSampleTask(createdBy = UUID.randomUUID())

        // When
        val createdTask = taskRepository.createTask(initialTask)

        // Then
        assertThat(createdTask.createdBy).isEqualTo(initialTask.createdBy)
    }

    @Test
    fun `should call mapper to map task entity to row`() {
        // Given
        val task = createSampleTask()

        // When
        taskRepository.createTask(task)

        // Then
        verify(exactly = 1) { mockTaskMapper.mapEntityToRow(any()) }
    }

    @Test
    fun `should call data source to add the mapped task`() {
        // Given
        val task = createSampleTask()
        val mappedTask = listOf("mapped-task-data")
        every { mockTaskMapper.mapEntityToRow(any()) } returns mappedTask

        // When
        taskRepository.createTask(task)

        // Then
        verify(exactly = 1) { mockTaskDataSource.addTask(mappedTask) }
    }

    private fun createSampleTask(id: UUID = UUID.randomUUID(),
        title: String = "Test Task",
        description: String = "Test Description",
        projectId: UUID = UUID.randomUUID(),
        stateId: UUID = UUID.randomUUID(),
        assignedTo: UUID? = UUID.randomUUID(),
        createdBy: UUID = UUID.randomUUID(),
        createdAt: LocalDateTime = LocalDateTime.now().minusDays(1),
        updatedAt: LocalDateTime = LocalDateTime.now().minusDays(1)): Task {


        return Task(id = id,
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo,
            createdBy = createdBy,
            createdAt = createdAt,
            updatedAt = updatedAt)
    }
}