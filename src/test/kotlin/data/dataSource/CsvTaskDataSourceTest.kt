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
    private val testFilePath = "build/test-resources/test_tasks.csv"
    private lateinit var dataSource: CsvTaskDataSource
    private lateinit var testFile: File

    @BeforeEach
    fun setup() {
        testFile = File(testFilePath).apply {
            parentFile.mkdirs()
            writeText("")
        }
        dataSource = CsvTaskDataSource(testFile)
    }

    @AfterEach
    fun teardown() {
        if (testFile.exists()) testFile.delete()
    }

    @Test
    fun `save should persist task and getAll should retrieve it`() {
        val task = sampleTask()

        dataSource.save(task)
        val tasks = dataSource.getAll()

        assertThat(tasks).hasSize(1)
        assertThat(tasks.first()).isEqualTo(task)
    }

    @Test
    fun `getById should return correct task`() {
        val task = sampleTask()
        dataSource.save(task)

        val found = dataSource.getById(task.id)

        assertNotNull(found)
        assertThat(found).isEqualTo(task)
    }

    @Test
    fun `getById should return null for non-existent task`() {
        dataSource.save(sampleTask())

        val result = dataSource.getById(UUID.randomUUID())

        assertNull(result)
    }

    @Test
    fun `overwriteAll should replace all existing tasks`() {
        val initialTasks = listOf(sampleTask(), sampleTask())
        dataSource.overwriteAll(initialTasks)

        val newTasks = listOf(sampleTask(title = "Task 1"), sampleTask(title = "Task 2"))
        dataSource.overwriteAll(newTasks)

        val result = dataSource.getAll()
        assertThat(result).hasSize(2)
        assertThat(result.map { it.title }).containsExactly("Task 1", "Task 2")
    }

    @Test
    fun `update should modify existing task`() {
        val original = sampleTask(title = "Original")
        dataSource.save(original)

        val updated = original.copy(title = "Updated")
        val success = dataSource.update(updated)


        assertThat(dataSource.getById(original.id)?.title).isEqualTo("Updated")
    }

    @Test
    fun `update should return false for non-existent task`() {
        val success = dataSource.update(sampleTask())

        assertThat(success).isEqualTo(false)
    }

    @Test
    fun `delete should remove task`() {
        val task = sampleTask()
        dataSource.save(task)

        val success = dataSource.delete(task.id)
        val tasks = dataSource.getAll()

        assertThat(tasks).isEmpty()
    }

    @Test
    fun `delete should return false for non-existent task`() {
        val success = dataSource.delete(UUID.randomUUID())

        assertThat(success).isEqualTo(false)
    }

    @Test
    fun `should handle special characters in fields`() {
        val task = sampleTask(
            title = "Task, with, commas",
            description = "Description with \"quotes\"",
            assignedTo = "User, with, comma"
        )

        dataSource.save(task)
        val retrieved = dataSource.getById(task.id)

        assertNotNull(retrieved)
        assertThat(retrieved.title).isEqualTo("Task, with, commas")
        assertThat(retrieved.description).isEqualTo("Description with \"quotes\"")
        assertThat(retrieved.assignedTo).isEqualTo("User, with, comma")
    }

    @Test
    fun `getAll should throw when file does not exist`() {
        val nonExistentFile = File("build/test-resources/non_existing_file.csv")
        if (nonExistentFile.exists()) nonExistentFile.delete()

        val exception = assertThrows<IllegalStateException> {
            CsvTaskDataSource(nonExistentFile).getAll()
        }

        assertThat(exception.message).contains("CSV file not found")
    }

    @Test
    fun `getById should throw when file does not exist`() {
        val nonExistentFile = File("build/test-resources/missing_tasks.csv")
        if (nonExistentFile.exists()) nonExistentFile.delete()

        val exception = assertThrows<IllegalStateException> {
            CsvTaskDataSource(nonExistentFile).getById(UUID.randomUUID())
        }

        assertThat(exception.message).contains("CSV file not found")
    }
    @Test
    fun `parse should return null for malformed CSV rows`() {
        // Create a malformed CSV row (missing fields)
        val malformedRow = listOf(
            UUID.randomUUID().toString(),
            "Test Task"
            // Missing all other required fields
        )

        // Use reflection to access private parse method
        val parseMethod = CsvTaskDataSource::class.java.getDeclaredMethod("parse", List::class.java)
        parseMethod.isAccessible = true

        // Invoke parse on the malformed row
        val result = parseMethod.invoke(dataSource, malformedRow)

        assertNull(result)
    }


    private fun sampleTask(
        title: String = "Sample Task",
        id: UUID = UUID.randomUUID(),
        description: String = "Test description",
        assignedTo: String? = null
    ): Task {
        val now = LocalDateTime.now()
        return Task(
            id = id,
            title = title,
            description = description,
            projectId = "proj-001",
            stateId = "todo",
            assignedTo = assignedTo,
            createdBy = "tester",
            createdAt = now,
            updatedAt = now
        )
    }
}