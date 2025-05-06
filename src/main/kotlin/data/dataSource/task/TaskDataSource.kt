package data.dataSource.task

interface TaskDataSource {

    suspend fun addTask(taskRow: List<String>): Boolean
    suspend fun getTasks(): List<List<String>>
    suspend fun getTaskById(taskId: String): List<String>?
    suspend fun deleteTask(taskId: String): Boolean
    suspend fun updateTask(taskRow: List<String>): Boolean
}