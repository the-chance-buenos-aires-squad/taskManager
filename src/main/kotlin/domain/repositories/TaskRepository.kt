package domain.repositories

import domain.entities.Task

interface TaskRepository {
    fun addTask(task: Task): Boolean

    fun getAllTasks():List<Task>
}