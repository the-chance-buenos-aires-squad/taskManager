package data.dataSource

import domain.entities.Task
import java.util.*
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
    import java.util.*

    class CsvTaskDataSource(private val filePath: String = "data/resource/CSVfiles/tasks.csv") {

        private val delimiter = ","

        fun save(task: Task) {
            ensureFileReady()

            try {
                File(filePath).appendText(serialize(task) + "\n")
            } catch (e: Exception) {
                throw IllegalStateException("Failed to save task: ${e.message}", e)
            }
        }

        fun getAll(): List<Task> {
            val file = File(filePath)
            if (!file.exists()) throw IllegalStateException("CSV file not found at path: $filePath")

            return try {
                file.readLines()
                    .filter { it.isNotBlank() }
                    .mapNotNull { parse(it) }
            } catch (e: Exception) {
                throw IllegalStateException("Failed to read tasks: ${e.message}", e)
            }
        }

        fun overwriteAll(tasks: List<Task>) {
            val file = ensureFileReady()

            try {
                file.writeText("")
                tasks.forEach { task ->
                    file.appendText(serialize(task) + "\n")
                }
            } catch (e: Exception) {
                throw IllegalStateException("Failed to overwrite tasks at $filePath: ${e.message}", e)
            }
        }

        fun getById(taskId: UUID): Task? {
            return getAll().find { it.id == taskId }
        }

        fun update(task: Task) {
            val tasks = getAll().toMutableList()
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index != -1) {
                tasks[index] = task
                overwriteAll(tasks)
            }
        }

        fun delete(taskId: UUID) {
            val tasks = getAll().filterNot { it.id == taskId }
            overwriteAll(tasks)
        }

        // ------------------- Helpers ------------------------

        private fun ensureFileReady(): File {
            val file = File(filePath)

            if (!file.exists()) {
                val parent = file.parentFile
                if (!parent.exists() && !parent.mkdirs()) {
                    throw IllegalStateException("Failed to create parent directory: ${parent.path}")
                }

                try {
                    if (!file.createNewFile()) {
                        throw IllegalStateException("Failed to create file: $filePath")
                    }
                } catch (e: IOException) {
                    throw IllegalStateException("Failed to create file: ${e.message}", e)
                }
            }

            return file
        }

        private fun serialize(task: Task): String {
            return listOf(
                task.id.toString(),
                task.title.escape(),
                task.description.escape(),
                task.projectId.escape(),
                task.stateId.escape(),
                task.assignedTo.orEmpty().escape(),
                task.createdBy.escape(),
                task.createdAt.toString(),
                task.updatedAt.toString()
            ).joinToString(delimiter)
        }

        private fun parse(line: String): Task? {
            val parts = line.split(delimiter)
            return try {
                Task(
                    id = UUID.fromString(parts[0]),
                    title = parts[1].unescape(),
                    description = parts[2].unescape(),
                    projectId = parts[3].unescape(),
                    stateId = parts[4].unescape(),
                    assignedTo = parts[5].ifBlank { null }?.unescape(),
                    createdBy = parts[6].unescape(),
                    createdAt = LocalDateTime.parse(parts[7]),
                    updatedAt = LocalDateTime.parse(parts[8])
                )
            } catch (e: Exception) {
                null
            }
        }

        private fun String.escape(): String = this.replace(",", "%2C")
        private fun String.unescape(): String = this.replace("%2C", ",")
    }
