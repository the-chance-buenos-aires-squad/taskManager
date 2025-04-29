package data.dataSource

import domain.entities.Task
import java.util.*

class CsvTaskDataSource(private val filePath: String = "data/resource/CSVfiles/tasks.csv") {
    fun save(task: Task) { /* Empty */ }
    fun getAll(): List<Task> = emptyList()
    fun overwriteAll(tasks: List<Task>) { /* Empty */ }
    fun getById(taskId: UUID): Task? = null
    fun update(task: Task) { /* Empty */ }
    fun delete(taskId: UUID) { /* Empty */ }

}