package data.dataSource

import data.dataSource.util.CsvHandler
import domain.entities.Task
import java.io.File
import java.time.LocalDateTime
import java.util.*

class CsvTaskDataSource(
    private val file: File,
    private val csvHandler: CsvHandler = CsvHandler(
        com.github.doyaaaaaken.kotlincsv.client.CsvWriter(),
        com.github.doyaaaaaken.kotlincsv.client.CsvReader()
    )
) {
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

    fun getById(taskId: UUID): Task? {
        return getAll().find { it.id == taskId }
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


    fun delete(taskId: UUID): Boolean {
        val tasks = getAll()
        val existedBefore = tasks.any { it.id == taskId }
        if (!existedBefore) {
            return false
        }
        val newTasks = tasks.filterNot { it.id == taskId }
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
