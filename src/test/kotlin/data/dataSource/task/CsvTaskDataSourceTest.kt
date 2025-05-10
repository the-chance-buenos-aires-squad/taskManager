package data.dataSource.task

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import dummyData.DummyTasks
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class CsvTaskDataSourceTest {

    private lateinit var file: File
    private val csvHandler = CsvHandler(CsvReader())
    private val parser = TaskDtoParser()
    private lateinit var dataSource: CsvTaskDataSource


    @BeforeEach
    fun setup() {
        file = File.createTempFile("task_test", ".csv")
        file.writeText("")
        dataSource = CsvTaskDataSource(csvHandler = csvHandler, taskDtoParser = parser, file = file)


    }

    @Test
    fun `addTask should write task and return true`() = runTest {

        val result = dataSource.addTask(DummyTasks.validTaskDto)
        assertThat(result).isTrue()
        assertThat(dataSource.getTasks()).contains(DummyTasks.validTaskDto)
    }

    @Test
    fun `getTasks should return all written tasks`() = runTest {

        dataSource.addTask(DummyTasks.validTaskDto)
        dataSource.addTask(DummyTasks.validTaskDto)
        val tasks = dataSource.getTasks()
        assertThat(tasks).hasSize(2)
    }

    @Test
    fun `getTaskById should return correct row`() = runTest {

        dataSource.addTask(DummyTasks.validTaskDto)
        val fetched = dataSource.getTaskById(DummyTasks.validTask.id)
        assertThat(fetched).isEqualTo(DummyTasks.validTaskDto)
    }

    @Test
    fun `updateTask should return false if id not found`() = runTest {

        val result = dataSource.updateTask(DummyTasks.validTaskDto)
        assertThat(result).isFalse()
    }

    @Test
    fun `updateTask should modify row and return true`() = runTest {
        val original = DummyTasks.validTaskDto
        dataSource.addTask(original)
        val updated = original.copy(title = "Updated Title")
        val result = dataSource.updateTask(updated)
        assertThat(result).isTrue()
        val fetched = dataSource.getTaskById(UUID.fromString(updated.id))
        assertThat(fetched).isEqualTo(updated)
    }

    @Test
    fun `deleteTask should return false if id not found`() = runTest {
        val result = dataSource.deleteTask(DummyTasks.validTask.id)
        assertThat(result).isFalse()
    }

    @Test
    fun `deleteTask should remove row and return true`() = runTest {
        val task = DummyTasks.validTaskDto
        dataSource.addTask(task)
        val result = dataSource.deleteTask(UUID.fromString(task.id))
        assertThat(result).isTrue()
        assertThat(dataSource.getTasks()).doesNotContain(task)
    }
}