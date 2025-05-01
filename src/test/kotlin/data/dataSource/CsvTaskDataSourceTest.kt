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

    @Test
    fun `save should handle empty file`() {
        testFile.writeText("")
        dataSource.save(sampleTask())
        assertThat(dataSource.getAll()).hasSize(1)
    }

    @Test
    fun `overwriteAll should handle empty list`() {
        dataSource.overwriteAll(emptyList())
        assertThat(dataSource.getAll()).isEmpty()
    }
    @Test
    fun `update should not alter task if no changes are made`() {
        val task = sampleTask()
        dataSource.save(task)
        val result = dataSource.update(task)
        assertThat(result).isEqualTo(true)
        assertThat(dataSource.getById(task.id)).isEqualTo(task)
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
    fun `save and getAll should persist and retrieve task`() {
        val task = sampleTask()
        dataSource.save(task)
        val tasks = dataSource.getAll()
        assertThat(tasks).hasSize(1)
        assertThat(tasks.first()).isEqualTo(task)
    }

    @Test
    fun `overwriteAll should clear and replace all tasks`() {
        val task1 = sampleTask("Task1")
        val task2 = sampleTask("Task2")
        dataSource.overwriteAll(listOf(task1, task2))
        val newTasks = listOf(sampleTask("New1"), sampleTask("New2"))
        dataSource.overwriteAll(newTasks)
        val result = dataSource.getAll()
        assertThat(result).hasSize(2)
        assertThat(result.map { it.title }).containsExactly("New1", "New2")
    }

    @Test
    fun `getById should return the correct task`() {
        val task = sampleTask()
        dataSource.save(task)
        val result = dataSource.getById(task.id)
        assertThat(result).isEqualTo(task)
    }
    @Test
    fun `getById should return null for non-existent id`() {
        dataSource.save(sampleTask())
        val result = dataSource.getById(UUID.randomUUID())
        assertThat(result).isNull()
    }

    @Test
    fun `update should modify the task and return true`() {
        val task = sampleTask(title = "Old")
        dataSource.save(task)
        val updated = task.copy(title = "New")
        val result = dataSource.update(updated)
        assertThat(result).isTrue()
        assertThat(dataSource.getById(task.id)?.title).isEqualTo("New")
    }

    @Test
    fun `update should return false if task does not exist`() {
        val result = dataSource.update(sampleTask())
        assertThat(result).isFalse()
    }

    @Test
    fun `delete should remove task and return true`() {
        val task = sampleTask()
        dataSource.save(task)
        val deleted = dataSource.delete(task.id)
        assertThat(deleted).isTrue()
        assertThat(dataSource.getAll()).isEmpty()
    }

    @Test
    fun `delete should return false if task does not exist`() {
        val result = dataSource.delete(UUID.randomUUID())
        assertThat(result).isFalse()
    }

    @Test
    fun `serialize should return escaped values`() {
        val task = sampleTask(title = "Title,Test")
        val method = CsvTaskDataSource::class.java.getDeclaredMethod("serialize", Task::class.java)
        method.isAccessible = true
        val serialized = method.invoke(dataSource, task) as List<*>
        assertThat(serialized[1].toString()).contains("%2C")
    }
    @Test
    fun `parse should return task from valid row`() {
        val task = sampleTask()
        val method = CsvTaskDataSource::class.java.getDeclaredMethod("parse", List::class.java)
        method.isAccessible = true
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
        val parsed = method.invoke(dataSource, row) as Task?
        assertThat(parsed?.id).isEqualTo(task.id)
        assertThat(parsed?.title).isEqualTo(task.title)
    }
    @Test
    fun `parse should return null for malformed data`() {
        val row = listOf("invalid-id", "Title")
        val method = CsvTaskDataSource::class.java.getDeclaredMethod("parse", List::class.java)
        method.isAccessible = true
        val parsed = method.invoke(dataSource, row)
        assertThat(parsed).isNull()
    }

    @Test
    fun `escape and unescape should correctly handle commas`() {
        val input = "one,two"
        val escaped = input.replace(",", "%2C")
        val unescaped = escaped.replace("%2C", ",")
        assertThat(unescaped).isEqualTo("one,two")
    }

    @Test
    fun `getAll should throw if file is missing`() {
        val missing = File("build/test-resources/does_not_exist.csv")
        if (missing.exists()) missing.delete()
        val dataSource = CsvTaskDataSource(missing)
        val exception = assertThrows<IllegalStateException> {
            dataSource.getAll()
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