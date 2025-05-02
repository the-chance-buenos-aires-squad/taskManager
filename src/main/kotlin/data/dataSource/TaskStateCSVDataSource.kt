package data.dataSource
import data.mapper.StateMapper
import java.io.File
import data.util.CsvHandler
import domain.entities.State
import domain.entities.StateIndices

class TaskStateCSVDataSource(
    private val file: File,
    private val csvHandler: CsvHandler
): TaskStateDataSource {

    override fun createTaskState(state: State): Boolean {
        val existingTaskStates = getAllTaskStates()
        if (existingTaskStates.any { it.id == state.id }) return false

        val allStates = existingTaskStates + state
        writeTaskStates(allStates)
        return true
    }

    override fun editTaskState(editState: State): Boolean {
        val allStates = getAllTaskStates().toMutableList()
        println(allStates)
        val index = allStates.indexOfFirst { it.id == editState.id }

        return if (index != -1) {
            allStates[index] = editState
            writeTaskStates(allStates)
            true
        } else {
            false
        }
    }

    override fun deleteTaskState(stateId: String): Boolean {
        val states = getAllTaskStates().toMutableList()
        val removed = states.removeIf { it.id == stateId }

        if (removed) {
            writeTaskStates(states)
            return true
        }
        return false
    }

    override fun getAllTaskStates(): List<State> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file)
            .mapNotNull { parts -> StateMapper.map(parts)}
    }

    override fun existsTaskState(stateId: String): Boolean {
        val allStates = getAllTaskStates()
        return allStates.any { it.id == stateId }
    }

    private fun writeTaskStates(states: List<State>) {
        file.writeText("")
        try {
            states.forEach { state ->
                csvHandler.write(
                    listOf(state.id, state.name, state.projectId),
                    file,
                    append = true
                )
            }
        } catch (e: Exception) {
            println("Failed to write states: ${e.message}")
        }
    }

}


