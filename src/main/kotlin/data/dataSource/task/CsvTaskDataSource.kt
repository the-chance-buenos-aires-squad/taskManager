package data.dataSource.task

import data.dataSource.util.CsvHandler
import java.io.File

class CsvTaskDataSource(
    private val csvHandler: CsvHandler,
    private val file: File
) : TaskDataSource {

    override fun addTask(taskRow: List<String>): Boolean {
        return try {
            csvHandler.write(row = taskRow, file = file, append = true)
            true
        } catch (e: Exception) {
            println("Failed to write task: ${e.message}")
            false
        }
    }

    override fun getTasks(): List<List<String>> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file)
    }

    override fun getTaskById(taskId: String): List<String>? {
        return getTasks().find { it[ID_INDEX] == taskId }
    }

    override fun updateTask(taskRow: List<String>): Boolean {
        val allTasks = getTasks()
        val exists = allTasks.any { it[ID_INDEX] == taskRow[ID_INDEX] }

        if (!exists) return false

        val updatedTasks = allTasks
            .filterNot { it[ID_INDEX] == taskRow[ID_INDEX] }
            .toMutableList()
            .apply { add(taskRow) }

        return try {
            file.writeText("") // Clear file
            updatedTasks.forEach { row ->
                csvHandler.write(row = row, file = file, append = true)
            }
            true
        } catch (e: Exception) {
            println("Error updating task: ${e.message}")
            false
        }
    }

    override fun deleteTask(taskId: String): Boolean {
        val allTasks = getTasks()
        val updatedTasks = allTasks.filterNot { it[ID_INDEX] == taskId }

        if (allTasks.size == updatedTasks.size) return false

        return try {
            file.writeText("") // Clear file
            updatedTasks.forEach { row ->
                csvHandler.write(row = row, file = file, append = true)
            }
            true
        } catch (e: Exception) {
            println("Error deleting task: ${e.message}")
            false
        }
    }

    companion object {
        private const val ID_INDEX = 0
    }
}
