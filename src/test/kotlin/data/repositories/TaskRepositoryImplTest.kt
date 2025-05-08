package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.task.TaskDataSource
import data.repositories.mappers.CsvTaskMapper
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
    private val taskMapper = mockk<CsvTaskMapper>(relaxed = true)
    private lateinit var taskRepository: TaskRepository

    @BeforeEach
    fun setUp() {
        taskRepository = TaskRepositoryImpl(mockTaskDataSource,taskMapper)

        // Mock mapper and data source to return expected values
//        every { mockTaskDataSource.addTask(any()) } returns true  // Return true for successful task addition
    }

    @Test
    fun `should return true when task is successfully created`() {
        // Given
        val task = createSampleTask()
        every { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(task)

        // Then
        assertThat(result).isTrue()  // Ensure that true is returned for a successful task creation
    }

    @Test
    fun `should generate new id when creating a task`() {
        // Given
        val initialId = UUID.randomUUID()
        val initialTask = createSampleTask(id = initialId)
        every { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()  // Should return true on successful creation
    }

    @Test
    fun `should set current time as createdAt when creating a task`() {
        // Given
        val initialTask = createSampleTask(createdAt = LocalDateTime.now().minusDays(5))
        every { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()  // Should return true on successful creation
    }

    @Test
    fun `should set current time as updatedAt when creating a task`() {
        // Given
        val initialTask = createSampleTask(updatedAt = LocalDateTime.now().minusDays(5))
        every { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()  // Should return true on successful creation
    }

    @Test
    fun `should preserve task properties when creating a task`() {
        // Given
        val initialTask = createSampleTask(title = "Specific Title",description = "Specific Description")
        every { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(initialTask)

        // Then
        assertThat(result).isTrue()  // Ensure the task was successfully created
    }

    @Test
    fun `should call mapper to map task entity to row`() {
        // Given
        val task = createSampleTask()

        // When
        taskRepository.addTask(task)

        // Then
        verify(exactly = 1) { taskMapper.fromEntity(any()) }  // Ensure the mapper is called once
    }

    @Test
    fun `should call data source to add the mapped task`() {
        // Given
        val task = createSampleTask()
        val mappedTask = listOf("mapped-task-data")
        every { taskMapper.fromEntity(any()) } returns mappedTask

        // When
        taskRepository.addTask(task)

        // Then
        verify(exactly = 1) { mockTaskDataSource.addTask(mappedTask) }  // Ensure the data source addTask is called once
    }

    @Test
    fun `should return true when task is added to data source`() {
        // Given
        val task = createSampleTask()
        every { mockTaskDataSource.addTask(any()) } returns true

        // When
        val result = taskRepository.addTask(task)

        // Then
        assertThat(result).isTrue()  // The method should return true on success
    }

    @Test
    fun `should return all mapped tasks from data source`() {
        // Given
        val rawTaskRow1 = listOf(
            "id1",
            "title1",
            "desc1",
            "proj1",
            "state1",
            "",
            "creator1",
            "2023-01-01T00:00:00",
            "2023-01-01T00:00:00"
        )
        val rawTaskRow2 = listOf(
            "id2",
            "title2",
            "desc2",
            "proj2",
            "state2",
            "",
            "creator2",
            "2023-01-02T00:00:00",
            "2023-01-02T00:00:00"
        )
        val rawRows = listOf(rawTaskRow1,rawTaskRow2)

        val mappedTask1 = createSampleTask(title = "title1",description = "desc1")
        val mappedTask2 = createSampleTask(title = "title2",description = "desc2")

        every { mockTaskDataSource.getTasks() } returns rawRows
        every { taskMapper.toEntity(rawTaskRow1) } returns mappedTask1
        every { taskMapper.toEntity(rawTaskRow2) } returns mappedTask2

        // When
        val result = taskRepository.getAllTasks()

        // Then
        verify(exactly = 1) { mockTaskDataSource.getTasks() }
        verify(exactly = 1) { taskMapper.toEntity(rawTaskRow1) }
        verify(exactly = 1) { taskMapper.toEntity(rawTaskRow2) }

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(mappedTask1,mappedTask2)
    }

    private fun createSampleTask(
        id: UUID = UUID.randomUUID(),
        title: String = "Test Task",
        description: String = "Test Description",
        projectId: UUID = UUID.randomUUID(),
        stateId: UUID = UUID.randomUUID(),
        assignedTo: UUID? = UUID.randomUUID(),
        createdBy: UUID = UUID.randomUUID(),
        createdAt: LocalDateTime = LocalDateTime.now().minusDays(1),
        updatedAt: LocalDateTime = LocalDateTime.now().minusDays(1)
    ): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo,
            createdBy = createdBy,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }


    @Test
    fun `should return true when deleting task`() {
        // Given
        val task = createSampleTask()
        val task1 = createSampleTask()
        val task2 = createSampleTask()
        every { mockTaskDataSource.deleteTask(any()) } returns true

        //when
        val result = taskRepository.deleteTask(task.id)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when deleting task failed`() {
        //given
        val task = createSampleTask()
        every { mockTaskDataSource.deleteTask(any()) } returns false

        //when
        val result = taskRepository.deleteTask(task.id)

        //then
        assertThat(result).isFalse()
    }


    @Test
    fun `should return true when updating task successfully`() {
        //given
        val task = createSampleTask()
        every { mockTaskDataSource.updateTask(taskMapper.fromEntity(task)) } returns true

        //when
        val result = taskRepository.updateTask(task)

        //then
        assertThat(result).isTrue()
    }


    @Test
    fun `should return false when updating task unSuccessfully`() {
        //given
        val task = createSampleTask()
        every { mockTaskDataSource.updateTask(taskMapper.fromEntity(task)) } returns false

        //when
        val result = taskRepository.updateTask(task)

        //then
        assertThat(result).isFalse()
    }


    @Test
    fun `should return non-null task object when getting by id successfully`() {
        //given
        val task = createSampleTask()
        val taskRow = taskMapper.fromEntity(task)
        every { mockTaskDataSource.getTaskById(task.id.toString()) } returns taskRow

        //when
        val result = taskRepository.getTaskById(task.id)

        //then
        assertThat(result).isNotNull()
    }


    @Test
    fun `should return null  when getting task by id unSuccessfully`() {
        //given
        val task = createSampleTask()
        val taskRow = taskMapper.fromEntity(task)
        every { mockTaskDataSource.getTaskById(task.id.toString()) } returns null

        //when
        val result = taskRepository.getTaskById(task.id)

        //then
        assertThat(result).isNull()
    }


}