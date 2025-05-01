package data.dataSource

import data.dataSource.util.CsvHandler
import domain.entities.Task
import di.Paths
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.component.get
import org.koin.core.qualifier.named
import java.io.File
import java.time.LocalDateTime
import java.util.*

class CsvTaskDataSource :  KoinComponent{

    private val file: File by inject(named(Paths.TaskFilePath))
    private val csvHandler: CsvHandler by inject()

    fun save(task: Task) {
        csvHandler.write(serialize(task), file, append = true)
    }

    fun getAll(): List<Task> {
        if (!file.exists()) throw IllegalStateException("CSV file not found: ${file.path}")
        return csvHandler.read(file).mapNotNull(::parse)
    }

    fun overwriteAll(tasks: List<Task>) {
        file.writeText("") // Clear file
        tasks.forEach { save(it) }
    }

    fun getById(taskId: String): Task? {
        return try {
            val uuid = UUID.fromString(taskId)
            getAll().find { it.id == uuid }
        } catch (e: IllegalArgumentException) {
            null // Return null if the string is not a valid UUID
        }
    }

    fun update(updatedTask: Task): Boolean {
        val tasks = getAll().toMutableList()
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        return if (index != -1) {
            tasks[index] = updatedTask
            overwriteAll(tasks)
            true
        } else {
            false
        }
    }

    fun delete(taskId: String): Boolean {
        val uuid = try {
            UUID.fromString(taskId)
        } catch (e: IllegalArgumentException) {
            return false
        }

        val tasks = getAll()
        val existedBefore = tasks.any { it.id == uuid }
        if (!existedBefore) return false

        val newTasks = tasks.filterNot { it.id == uuid }
        overwriteAll(newTasks)
        return true
    }

    private fun serialize(task: Task): List<String> = listOf(
        task.id.toString(),
        task.title.escape(),
        task.description.escape(),
        task.projectId.escape(),
        task.stateId.escape(),
        task.assignedTo.orEmpty().escape(),
        task.createdBy.escape(),
        task.createdAt.toString(),
        task.updatedAt.toString()
    )

    private fun parse(row: List<String>): Task? {
        return try {
            Task(
                id = UUID.fromString(row[0]),
                title = row[1].unescape(),
                description = row[2].unescape(),
                projectId = row[3].unescape(),
                stateId = row[4].unescape(),
                assignedTo = row[5].ifBlank { null }?.unescape(),
                createdBy = row[6].unescape(),
                createdAt = LocalDateTime.parse(row[7]),
                updatedAt = LocalDateTime.parse(row[8])
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun String.escape(): String = replace(",", "%2C")
    private fun String.unescape(): String = replace("%2C", ",")
}
