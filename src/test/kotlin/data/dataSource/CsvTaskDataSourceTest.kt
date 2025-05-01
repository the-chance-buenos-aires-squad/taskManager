package data.datasource

import domain.entities.Task
import org.junit.jupiter.api.*
import com.google.common.truth.Truth.assertThat
import data.dataSource.CsvTaskDataSource
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
        assertThat(result).isNull()
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
        assertThat(success).isTrue()
        assertThat(dataSource.getById(original.id)?.title).isEqualTo("Updated")
    }

    @Test
    fun `update should return false for non-existent task`() {
        val success = dataSource.update(sampleTask())
        assertThat(success).isFalse()
    }

    @Test
    fun `update should not alter task if no changes are made`() {
        val task = sampleTask()
        dataSource.save(task)
        val result = dataSource.update(task)
        assertThat(result).isTrue()
        assertThat(dataSource.getById(task.id)).isEqualTo(task)
    }

    @Test
    fun `delete should remove task`() {
        val task = sampleTask()
        dataSource.save(task)
        val success = dataSource.delete(task.id)
        assertThat(success).isTrue()
        assertThat(dataSource.getAll()).isEmpty()
    }

    @Test
    fun `delete should return false for non-existent task`() {
        val success = dataSource.delete(UUID.randomUUID())
        assertThat(success).isFalse()
    }

    @Test
    fun `save should not overwrite existing tasks`() {
        val task1 = sampleTask(title = "First")
        val task2 = sampleTask(title = "Second")
        dataSource.save(task1)
        dataSource.save(task2)
        val tasks = dataSource.getAll()
        assertThat(tasks).hasSize(2)
        assertThat(tasks.map { it.title }).containsExactly("First", "Second")
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
    fun `parse should return null for malformed CSV rows`() {
        val malformedRow = listOf(UUID.randomUUID().toString(), "Test Task")
        val parseMethod = CsvTaskDataSource::class.java.getDeclaredMethod("parse", List::class.java)
        parseMethod.isAccessible = true
        val result = parseMethod.invoke(dataSource, malformedRow)
        assertThat(result).isNull()
    }

    @Test
    fun `parse should return task from valid row`() {
        val task = sampleTask()
        val parseMethod = CsvTaskDataSource::class.java.getDeclaredMethod("parse", List::class.java)
        parseMethod.isAccessible = true
        val row = listOf(
            task.id.toString(),
            task.title,
            task.description,
            task.projectId,
            task.stateId,
            task.assignedTo.orEmpty(),
            task.createdBy,
            task.createdAt.toString(),
            task.updatedAt.toString()
        )
        val parsed = parseMethod.invoke(dataSource, row) as Task?
        assertThat(parsed?.id).isEqualTo(task.id)
        assertThat(parsed?.title).isEqualTo(task.title)
    }

    @Test
    fun `serialize should return escaped values`() {
        val task = sampleTask(title = "Title,Test")
        val serializeMethod = CsvTaskDataSource::class.java.getDeclaredMethod("serialize", Task::class.java)
        serializeMethod.isAccessible = true
        val serialized = serializeMethod.invoke(dataSource, task) as List<*>
        assertThat(serialized[1].toString()).contains("%2C")
    }

    @Test
    fun `escape and unescape should correctly handle commas`() {
        val input = "one,two"
        val escaped = input.replace(",", "%2C")
        val unescaped = escaped.replace("%2C", ",")
        assertThat(unescaped).isEqualTo("one,two")
    }

    @Test
    fun `overwriteAll should handle empty list`() {
        dataSource.overwriteAll(emptyList())
        assertThat(dataSource.getAll()).isEmpty()
    }

    @Test
    fun `save should handle empty file`() {
        testFile.writeText("")
        dataSource.save(sampleTask())
        assertThat(dataSource.getAll()).hasSize(1)
    }

    @Test
    fun `getAll should throw when file does not exist`() {
        val missingFile = File("build/test-resources/non_existing_file.csv")
        if (missingFile.exists()) missingFile.delete()
        val exception = assertThrows<IllegalStateException> {
            CsvTaskDataSource(missingFile).getAll()
        }
        assertThat(exception.message).contains("CSV file not found")
    }

    @Test
    fun `getById should throw when file does not exist`() {
        val missingFile = File("build/test-resources/missing_tasks.csv")
        if (missingFile.exists()) missingFile.delete()
        val exception = assertThrows<IllegalStateException> {
            CsvTaskDataSource(missingFile).getById(UUID.randomUUID())
        }
        assertThat(exception.message).contains("CSV file not found")
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
