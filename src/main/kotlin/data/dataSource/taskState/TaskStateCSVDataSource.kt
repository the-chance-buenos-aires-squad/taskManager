package data.dataSource.taskState

import data.dataSource.util.CsvHandler
import data.repositories.mappers.TaskStateMapper
import data.repositories.mappers.TaskStateMapper.Companion.ID
import data.repositories.mappers.TaskStateMapper.Companion.NAME
import data.repositories.mappers.TaskStateMapper.Companion.PROJECT_ID
import domain.entities.TaskState
import java.io.File
import java.util.*

class TaskStateCSVDataSource(
    private val file: File,
    private val csvHandler: CsvHandler
) : TaskStateDataSource {

    override fun createTaskState(state: List<String>): Boolean {
        val existingTaskStates = getAllTaskStates()
        if (existingTaskStates.any { it.id.toString() == state[ID] }) return false

        val newTaskState = TaskState(
            id = UUID.fromString(state[ID]),
            name = state[NAME],
            projectId =  UUID.fromString(state[PROJECT_ID])
        )

        val allStates = existingTaskStates + newTaskState
        writeTaskStates(allStates)
        return true
    }

    override fun editTaskState(editState: List<String>): Boolean {
        val allStates = getAllTaskStates().toMutableList()
        val index = allStates.indexOfFirst { it.id.toString() == editState[ID] }

        return if (index != -1) {
            val editTaskState = TaskState(
                id = UUID.fromString(editState[ID]),
                name = editState[NAME],
                projectId = UUID.fromString(editState[PROJECT_ID])
            )

            allStates[index] = editTaskState
            writeTaskStates(allStates)
            true
        } else false
    }

    override fun deleteTaskState(stateId: String): Boolean {
        val states = getAllTaskStates().toMutableList()
        val removed = states.removeIf { it.id.toString() == stateId }

        if (removed) {
            writeTaskStates(states)
            return true
        }
        return false
    }

    override fun getAllTaskStates(): List<TaskState> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file)
            .mapNotNull { parts -> TaskStateMapper().mapRowToEntity(parts) }
    }

    override fun existsTaskState(stateId: String): Boolean {
        val allStates = getAllTaskStates()
        return allStates.any { it.id.toString()  == stateId }
    }

    private fun writeTaskStates(states: List<TaskState>) {
        file.writeText("")
        try {
            states.forEach { state ->
                csvHandler.write(
                    listOf(state.id.toString(), state.name, state.projectId.toString()),
                    file,
                    append = true
                )
            }
        } catch (e: Exception) {
            println("Failed to write states: ${e.message}")
        }
    }
}


