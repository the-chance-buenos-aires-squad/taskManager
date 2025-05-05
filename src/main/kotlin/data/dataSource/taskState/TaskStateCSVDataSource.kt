package data.dataSource.taskState

import data.dataSource.util.CsvHandler
import data.repositories.mappers.TaskStateMapper
import data.repositories.mappers.TaskStateMapper.Companion.ID
import data.repositories.mappers.TaskStateMapper.Companion.NAME
import data.repositories.mappers.TaskStateMapper.Companion.PROJECT_ID
import java.io.File
import domain.entities.TaskState
import java.util.*

class TaskStateCSVDataSource(
    private val file: File,
    private val csvHandler: CsvHandler
) : TaskStateDataSource {

    override fun createTaskState(state: List<String>): Boolean {
        val allTaskStates = getAllTaskStates()
        val newTaskState = TaskState(
            name = state[NAME],
            projectId = state[PROJECT_ID]
        )

        val allStates = allTaskStates + newTaskState
        writeTaskStates(allStates)
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
                projectId = editState[PROJECT_ID]
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

    override fun existsTaskState(name: String, projectId: String): Boolean {
        val allStates = getAllTaskStates()
        return allStates.any { it.name == name && it.projectId == projectId }
    }

    private fun writeTaskStates(states: List<TaskState>) {
        try {
            file.writeText("")
            states.forEach { state ->
                csvHandler.write(
                    listOf(state.id.toString(), state.name, state.projectId),
                    file,
                    append = true
                )
            }
        } catch (e: Exception) {
            println("Failed to write states: ${e.message}")
        }
    }
}


