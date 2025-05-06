package data.dataSource.task

interface TaskDataSource {
    fun addTask(taskRow: List<String>): Boolean
    fun getTasks(): List<List<String>>
    fun getTaskById(taskId: String): List<String>?
    fun deleteTask(taskId: String): Boolean
    fun updateTask(taskRow: List<String>): Boolean
}