package data.dataSource.taskState

import data.dataSource.util.CsvHandler
import data.dto.TaskStateDto
import java.io.File
import java.util.*

class TaskStateCSVDataSource(
    private val file: File,
    private val taskStateDtoParser: TaskStateDtoParser,
    private val csvHandler: CsvHandler
) : TaskStateDataSource {

    override suspend fun createTaskState(state: TaskStateDto): Boolean {
        csvHandler.write(taskStateDtoParser.fromDto(state), file)
        return true
    }

    override suspend fun editTaskState(editState: TaskStateDto): Boolean {
        val allStates = getTaskStates().toMutableList()
        val index = allStates.indexOfFirst { it.id == editState.id }

        return if (index != -1) {
            val editTaskState = TaskStateDto(
                id = editState.id,
                name = editState.name,
                projectId = editState.projectId
            )

            allStates[index] = editTaskState
            writeTaskStates(allStates)

            true
        } else {
            false
        }
    }

    override suspend fun deleteTaskState(stateId: String): Boolean {
        val states = getTaskStates().toMutableList()
        val removed = states.removeIf { it.id == stateId.toString() }

        if (removed) {
            writeTaskStates(states)
            return true
        }
        return false
    }

    override suspend fun getTaskStates(): List<TaskStateDto> {
        return csvHandler.read(file)
            .mapNotNull { parts -> taskStateDtoParser.toDto(parts) }
    }

    override suspend fun existsTaskState(name: String, projectId: String): Boolean {
        val allStates = getTaskStates()
        return allStates.any { it.name.equals(name, ignoreCase = true) && it.projectId == projectId}
    }

    private fun writeTaskStates(states: List<TaskStateDto>) {
        file.writeText("")
        states.forEach { state ->
            csvHandler.write(
                listOf(state.id, state.name, state.projectId),
                file,
            )
        }
    }
}




