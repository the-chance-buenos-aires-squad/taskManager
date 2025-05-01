package domain.repositories

import domain.entities.Task

interface TaskRepository {
    fun createTask(task: Task): Task
}