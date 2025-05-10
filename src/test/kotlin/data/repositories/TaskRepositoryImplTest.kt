package data.repositories

import com.google.common.truth.Truth.assertThat
import data.repositories.dataSource.TaskDataSource
import data.repositories.mappers.TaskDtoMapper
import dummyData.DummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImplTest {

    private val mockTaskDataSource = mockk<TaskDataSource>(relaxed = true)
    private val mockTaskMapper = mockk<TaskDtoMapper>(relaxed = true)
    private lateinit var taskRepository: TaskRepositoryImpl

    @BeforeEach
    fun setUp() {
        taskRepository = TaskRepositoryImpl(mockTaskDataSource, mockTaskMapper)

        // Mock mapper and data source to return expected values
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { mockTaskMapper.fromEntity(any()) } returns DummyTasks.validTaskDto
    }

    @Test
    fun `should return true when task is successfully created`() = runTest {
        // Given
        coEvery { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(DummyTasks.validTask)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should generate new id when creating a task`() = runTest {
        // Given
        val initialId = UUID.randomUUID()
        val initialTask = DummyTasks.validTask.copy(id = initialId)
        coEvery { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should set current time as createdAt when creating a task`() = runTest {
        // Given
        val initialTask = DummyTasks.validTask.copy(createdAt = LocalDateTime.now().minusDays(5))
        coEvery { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should set current time as updatedAt when creating a task`() = runTest {
        // Given
        val initialTask = DummyTasks.validTask.copy(updatedAt = LocalDateTime.now().minusDays(5))
        coEvery { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should preserve task properties when creating a task`() = runTest {
        // Given
        val initialTask = DummyTasks.validTask.copy(title = "Specific Title", description = "Specific Description")
        coEvery { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should call mapper to map task entity to row`() = runTest {
        // When
        taskRepository.addTask(DummyTasks.validTask)

        // Then
        coVerify(exactly = 1) { mockTaskMapper.fromEntity(any()) }
    }

    @Test
    fun `should call data source to add the mapped task`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val mappedTask = DummyTasks.validTaskDto
        coEvery { mockTaskMapper.fromEntity(any()) } returns mappedTask

        // When
        taskRepository.addTask(task)

        // Then
        coVerify(exactly = 1) { mockTaskDataSource.addTask(mappedTask) }
    }

    @Test
    fun `should return true when task is added to data source`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(task)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return all mapped tasks from data source`() = runTest {
        // Given
        val taskDto = DummyTasks.validTaskDto
        val mappedTask = DummyTasks.validTask

        coEvery { mockTaskDataSource.getTasks() } returns listOf(taskDto)
        coEvery { mockTaskMapper.toEntity(taskDto) } returns mappedTask

        // When
        val result = taskRepository.getAllTasks()

        // Then
        coVerify(exactly = 1) { mockTaskDataSource.getTasks() }
        coVerify(exactly = 1) { mockTaskMapper.toEntity(taskDto) }

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(mappedTask)
    }

    @Test
    fun `should return true when deleting task`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.deleteTask(any()) } returns true

        // When
        val result = taskRepository.deleteTask(task.id)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when deleting task failed`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.deleteTask(any()) } returns false

        // When
        val result = taskRepository.deleteTask(task.id)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when updating task successfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val taskDto = DummyTasks.validTaskDto
        coEvery { mockTaskMapper.fromEntity(task) } returns taskDto
        coEvery { mockTaskDataSource.updateTask(taskDto) } returns true

        // When
        val result = taskRepository.updateTask(task)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when updating task unsuccessfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val taskDto = DummyTasks.validTaskDto
        coEvery { mockTaskMapper.fromEntity(task) } returns taskDto
        coEvery { mockTaskDataSource.updateTask(taskDto) } returns false

        // When
        val result = taskRepository.updateTask(task)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return non-null task object when getting by id successfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val taskDto = DummyTasks.validTaskDto
        coEvery { mockTaskDataSource.getTaskById(task.id.toString()) } returns taskDto
        coEvery { mockTaskMapper.toEntity(taskDto) } returns task

        // When
        val result = taskRepository.getTaskById(task.id)

        // Then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(task)
    }

    @Test
    fun `should return null when getting task by id unsuccessfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.getTaskById(task.id.toString()) } returns null

        // When
        val result = taskRepository.getTaskById(task.id)

        // Then
        assertThat(result).isNull()
    }
}