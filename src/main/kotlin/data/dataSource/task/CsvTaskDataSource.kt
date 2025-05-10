package data.dataSource.task

import data.dataSource.util.CsvHandler
import data.dto.TaskDto
import data.repositories.dataSource.TaskDataSource
import java.io.File
import java.util.*

class CsvTaskDataSource(
    private val csvHandler: CsvHandler,
    private val taskDtoParser: TaskDtoParser,
    private val file: File
) : TaskDataSource {

    override suspend fun addTask(taskDto: TaskDto): Boolean {
        return try {
            csvHandler.write(
                row = taskDtoParser.fromDto(taskDto),
                file = file,
                append = true,
            )
            true
        } catch (e: Exception) {
            println("Failed to write task: ${e.message}")
            false
        }
    }

    override suspend fun getTasks(): List<TaskDto> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file).map { row -> taskDtoParser.toDto(row) }
    }

    override suspend fun getTaskById(taskId: String): TaskDto? {
        return getTasks().find { it.id == taskId}
    }

    override suspend fun updateTask(taskDto: TaskDto): Boolean {
        val allTasks = getTasks()
        val exists = allTasks.any { it.id == taskDto.id }

        if (!exists) return false

        val updatedTasks = allTasks
            .filterNot { it.id == taskDto.id }
            .toMutableList()
            .apply { add(taskDto) }

        return try {
            file.writeText("") // Clear file
            updatedTasks.forEach { updatedTask ->
                csvHandler.write(
                    row = taskDtoParser.fromDto(updatedTask),
                    file = file,
                    append = true,
                )
            }
            true
        } catch (e: Exception) {
            println("Error updating task: ${e.message}")
            false
        }
    }

    override suspend fun deleteTask(taskId: String): Boolean {
        val allTasks = this.getTasks()
        val updatedTasks = allTasks.filterNot { it.id == taskId }

        if (allTasks.size == updatedTasks.size) return false

        return try {
            file.writeText("") // Clear file
            updatedTasks.forEach { task ->
                csvHandler.write(
                    row = taskDtoParser.fromDto(task),
                    file = file,
                    append = true,
                )
            }
            true
        } catch (e: Exception) {
            println("Error deleting task: ${e.message}")
            false
        }
    }
}
