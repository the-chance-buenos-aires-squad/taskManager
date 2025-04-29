package domain.repositories

import domain.entities.Task

interface TaskRepository {

    fun createTask(task: Task): Task

    fun getTaskById(taskId: String): Task?

    fun getTasksByProject(projectId: String): List<Task>

    fun getTasksByState(projectId: String,stateId: String): List<Task>

    fun updateTask(task: Task): Task?

    fun updateTaskState(taskId: String,stateId: String): Task?

    fun deleteTask(taskId: String): Boolean

}