package data.dataSource.taskState

import data.dataSource.util.CsvHandler
import data.repositories.mappers.TaskStateMapper
import data.repositories.mappers.TaskStateMapper.Companion.ID
import data.repositories.mappers.TaskStateMapper.Companion.NAME
import data.repositories.mappers.TaskStateMapper.Companion.PROJECT_ID
import domain.entities.TaskState
import java.io.File
import java.util.UUID

class TaskStateCSVDataSource(
    private val file: File,
    private val csvHandler: CsvHandler
) : TaskStateDataSource {

    override fun createTaskState(state: List<String>): Boolean {
        csvHandler.write(state, file)
        return true
    }

    override fun editTaskState(editState: List<String>): Boolean {
        val allStates = getAllTaskStates().toMutableList()
        val editStateId = UUID.fromString(editState[ID])
        val index = allStates.indexOfFirst { it.id == editStateId }

        return if (index != -1) {
            val editTaskState = TaskState(
                id = editStateId,
                name = editState[NAME],
                projectId = UUID.fromString(editState[PROJECT_ID])
            )

            allStates[index] = editTaskState
            writeTaskStates(allStates)
            true
        } else false
    }

    override fun deleteTaskState(stateId: UUID): Boolean {
        val states = getAllTaskStates().toMutableList()
        val removed = states.removeIf { it.id == stateId }

        if (removed) {
            writeTaskStates(states)
            return true
        }
        return false
    }

    override fun getAllTaskStates(): List<TaskState> {
        return csvHandler.read(file)
            .mapNotNull { parts -> TaskStateMapper().mapRowToEntity(parts) }
    }

    override fun existsTaskState(name: String, projectId: UUID): Boolean {
        val allStates = getAllTaskStates()
        return allStates.any { it.name.equals(name, ignoreCase = true) && it.projectId == projectId }
    }

    private fun writeTaskStates(states: List<TaskState>) {
        file.writeText("")
        states.forEach { state ->
            csvHandler.write(
                listOf(state.id.toString(), state.name, state.projectId.toString()),
                file,
            )
        }
    }
}




