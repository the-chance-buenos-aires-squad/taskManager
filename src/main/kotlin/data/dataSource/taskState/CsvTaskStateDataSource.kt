package data.dataSource.taskState

import data.dataSource.util.CsvHandler
import data.dto.TaskStateDto
import data.repositories.dataSource.TaskStateDataSource
import java.io.File

class CsvTaskStateDataSource(
    private val file: File,
    private val taskStateDtoParser: TaskStateDtoParser,
    private val csvHandler: CsvHandler
) : TaskStateDataSource {

    override suspend fun createTaskState(state: TaskStateDto): Boolean {
        csvHandler.write(taskStateDtoParser.toType(state), file)
        return true
    }

    override suspend fun editTaskState(editState: TaskStateDto): Boolean {
        val allStates = getTaskStates().toMutableList()
        val index = allStates.indexOfFirst { it._id == editState._id }

        return if (index != -1) {
            val editTaskState = TaskStateDto(
                _id = editState._id,
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
        val removed = states.removeIf { it._id == stateId }

        if (removed) {
            writeTaskStates(states)
            return true
        }
        return false
    }

    override suspend fun getTaskStates(): List<TaskStateDto> {
        return csvHandler.read(file)
            .map { parts -> taskStateDtoParser.fromType(parts) }
    }

    private fun writeTaskStates(states: List<TaskStateDto>) {
        file.writeText("")
        states.forEach { state ->
            csvHandler.write(
                taskStateDtoParser.toType(state),
                file,
            )
        }
    }
}




