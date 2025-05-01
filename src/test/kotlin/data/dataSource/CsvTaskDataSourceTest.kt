package data.datasource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.CsvTaskDataSource
import data.dataSource.util.CsvHandler
import di.Paths
import domain.entities.Task
import org.junit.jupiter.api.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull

class CsvTaskDataSourceTest() {

    private lateinit var file: File
    private lateinit var dataSource: CsvTaskDataSource
    private val csvHandler = CsvHandler(CsvWriter(), CsvReader())

    @BeforeEach
    fun setUp() {
        file = File.createTempFile("task_test", ".csv").apply {
            writeText("")
            deleteOnExit()
        }
        dataSource = CsvTaskDataSource(csvHandler, file)
    }
    @Test
    fun `save should persist task and getAll should retrieve it`() {
        val task = sampleTask()
        dataSource.save(task)
        val tasks = dataSource.getAll()
        assertThat(tasks.first()).isEqualTo(task)
    }

    @Test
    fun `getById should return correct task`() {
        val task = sampleTask()
        dataSource.save(task)
        val found = dataSource.getById(task.id.toString())
        assertThat(found).isEqualTo(task)
    }

    @Test
    fun `overwriteAll should replace all existing tasks`() {
        val tasks = listOf(sampleTask("A"), sampleTask("B"))
        dataSource.overwriteAll(tasks)
        val result = dataSource.getAll()
        assertThat(result.map { it.title }).containsExactly("A", "B")
    }

    @Test
    fun `update should modify existing task`() {
        val task = sampleTask("Old")
        dataSource.save(task)
        val updated = task.copy(title = "New")
        val success = dataSource.update(updated)

        val result = dataSource.getById(task.id.toString())
        assertThat(success).isTrue()
        assertThat(result?.title).isEqualTo("New")
    }

    @Test
    fun `delete should remove task`() {
        val task = sampleTask()
        dataSource.save(task)
        val deleted = dataSource.delete(task.id.toString())
        assertThat(deleted).isTrue()
        assertThat(dataSource.getAll()).isEmpty()
    }

    @Test
    fun `parse should return null for malformed data`() {
        val malformedRow = listOf("invalid-uuid", "incomplete title")
        val method = CsvTaskDataSource::class.java.getDeclaredMethod("parse", List::class.java)
        method.isAccessible = true
        val result = method.invoke(dataSource, malformedRow)
        assertThat(result).isNull()
    }

    @Test
    fun `delete should return false for non-existent task`() {
        val randomId = UUID.randomUUID().toString()
        val result = dataSource.delete(randomId)
        assertThat(result).isFalse()
    }

    @Test
    fun `update should return false for non-existent task`() {
        val nonExistentTask = sampleTask()
        val result = dataSource.update(nonExistentTask)
        assertThat(result).isFalse()
    }
    @Test
    fun `delete should return false for invalid UUID string`() {
        val invalidId = "not-a-uuid"
        val result = dataSource.delete(invalidId)
        assertThat(result).isFalse()
    }
    @Test
    fun `delete should return false for valid UUID string that does not match any task`() {
        val nonExistentId = UUID.randomUUID().toString()
        val result = dataSource.delete(nonExistentId)
        assertThat(result).isFalse()
    }
    @Test
    fun `getById should return null and catch IllegalArgumentException for malformed UUID`() {
        val malformedId = "not-a-valid-uuid"

        val result = dataSource.getById(malformedId)

        assertThat(result).isNull()
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
