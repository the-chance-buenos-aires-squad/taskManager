package data.dataSource.task

import data.dto.TaskDto
import java.util.*

class FakeMongoTaskDataSource : TaskDataSource {
    private val tasks = mutableListOf<TaskDto>()

    override suspend fun addTask(taskDto: TaskDto): Boolean {
        return tasks.add(taskDto)
    }

    override suspend fun getTasks(): List<TaskDto> {
        return tasks.toList()
    }

    override suspend fun getTaskById(taskId: UUID): TaskDto? {
        return tasks.find { it.id == taskId.toString() }
    }

    override suspend fun deleteTask(taskId: UUID): Boolean {
        return tasks.removeIf { it.id == taskId.toString() }
    }

    override suspend fun updateTask(taskDto: TaskDto): Boolean {
        val index = tasks.indexOfFirst { it.id == taskDto.id }
        return if (index != -1) {
            tasks[index] = taskDto
            true
        } else {
            false
        }
    }
}