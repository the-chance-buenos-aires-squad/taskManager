package data.datasource

import domain.entities.Task
import org.junit.jupiter.api.*
import com.google.common.truth.Truth.assertThat
import data.dataSource.CsvTaskDataSource
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull


class CsvTaskDataSourceTest {

    private val testFilePath = "data/resource/test_tasks.csv"
    private lateinit var dataSource: CsvTaskDataSource

    @BeforeEach
    fun setup() {
        File(testFilePath).apply {
            parentFile.mkdirs()
            writeText("")
        }
        dataSource = CsvTaskDataSource(testFilePath)
    }

    @AfterEach
    fun teardown() {
        File(testFilePath).delete()
    }

    @Test
    fun `should save a task and retrieve it`() {
        // given
        val task = sampleTask()

        // when
        dataSource.save(task)
        val tasks = dataSource.getAll()

        // then
        assertThat(tasks).isNotEmpty()
    }

    @Test
    fun `should get a task by ID`() {
        // given
        val task = sampleTask()
        dataSource.save(task)

        // when
        val found = dataSource.getById(task.id)

        // then
        assertNotNull(found)
    }

    @Test
    fun `should overwrite all tasks`() {
        // given
        val tasks = listOf(sampleTask(), sampleTask(title = "Second"))

        // when
        dataSource.overwriteAll(tasks)
        val result = dataSource.getAll()

        // then
        assertThat(result).hasSize(2)

    }

    @Test
    fun `should update a task`() {
        // given
        val task = sampleTask(title = "Old Title")
        dataSource.save(task)

        // when
        val updatedTask = task.copy(title = "New Title")
        dataSource.update(updatedTask)

        val result = dataSource.getById(task.id)

        // then
        assertNotNull(result)
    }

    @Test
    fun `should delete a task`() {
        // given
        val task = sampleTask()
        dataSource.save(task)

        // when
        dataSource.delete(task.id)
        val tasks = dataSource.getAll()

        // then
        assertThat(tasks).isEmpty()
    }
    @Test
    fun `should throw exception if file does not exist when calling getAll`() {
        // given
        val nonExistentFilePath = "data/resource/non_existing_file.csv"
        val file = File(nonExistentFilePath)
        if (file.exists()) file.delete()

        val dataSource = CsvTaskDataSource(nonExistentFilePath)

        // when & then
        val exception = assertThrows<IllegalStateException> {
            dataSource.getAll()
        }

        assertThat(exception.message).contains("CSV file not found")
    }
    @Test
    fun `should throw exception if file does not exist when calling getById`() {
        // given
        val nonExistentFilePath = "data/resource/missing_tasks.csv"
        val file = File(nonExistentFilePath)
        if (file.exists()) file.delete()

        val dataSource = CsvTaskDataSource(nonExistentFilePath)
        val randomId = UUID.randomUUID()

        // when & then
        val exception = assertThrows<IllegalStateException> {
            dataSource.getById(randomId)
        }

        assertThat(exception.message).contains("CSV file not found")
    }
    private fun sampleTask(
        title: String = "Sample Task",
        id: UUID = UUID.randomUUID()
    ): Task {
        val now = LocalDateTime.now()
        return Task(
            id = id,
            title = title,
            description = "Test description",
            projectId = "proj-001",
            stateId = "todo",
            assignedTo = null,
            createdBy = "tester",
            createdAt = now,
            updatedAt = now
        )
    }
}