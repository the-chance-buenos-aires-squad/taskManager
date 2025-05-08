package data.dataSource.task

import com.google.common.truth.Truth.assertThat
import data.dto.TaskDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class MongoTaskDataSourceTest {

    private lateinit var dataSource: FakeMongoTaskDataSource
    private lateinit var sampleTaskDto: TaskDto
    private val taskId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        dataSource = FakeMongoTaskDataSource()

        sampleTaskDto = TaskDto(
            id = taskId.toString(),
            title = "Test Task",
            description = "Test Description",
            projectId = UUID.randomUUID().toString(),
            stateId = UUID.randomUUID().toString(),
            assignedTo = UUID.randomUUID().toString(),
            createdBy = UUID.randomUUID().toString(),
            createdAt = LocalDateTime.now().toString(),
            updatedAt = LocalDateTime.now().toString()
        )
    }

    @Test
    fun `addTask should return true when insertion is acknowledged`() = runTest {
        // When
        val result = dataSource.addTask(sampleTaskDto)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `getTasks should return list of tasks from collection`() = runTest {
        // Given
        dataSource.addTask(sampleTaskDto)

        // When
        val result = dataSource.getTasks()

        // Then
        assertThat(result).contains(sampleTaskDto)
    }

    @Test
    fun `getTaskById should return task when it exists`() = runTest {
        // Given
        dataSource.addTask(sampleTaskDto)

        // When
        val result = dataSource.getTaskById(taskId)

        // Then
        assertThat(result).isEqualTo(sampleTaskDto)
    }

    @Test
    fun `getTaskById should return null when task does not exist`() = runTest {
        // When
        val result = dataSource.getTaskById(taskId)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `deleteTask should return true when deletion is acknowledged`() = runTest {
        // Given
        dataSource.addTask(sampleTaskDto)

        // When
        val result = dataSource.deleteTask(taskId)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `deleteTask should return false when deletion is not acknowledged`() = runTest {
        // When
        val result = dataSource.deleteTask(taskId)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `updateTask should return true when update is acknowledged`() = runTest {
        // Given
        dataSource.addTask(sampleTaskDto)

        // When
        val updatedTask = sampleTaskDto.copy(title = "Updated Title")
        val result = dataSource.updateTask(updatedTask)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `updateTask should return false when update is not acknowledged`() = runTest {
        // When
        val result = dataSource.updateTask(sampleTaskDto)

        // Then
        assertThat(result).isFalse()
    }
}