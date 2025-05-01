package data.dataSource

import java.io.File
import data.util.CsvHandler
import domain.entities.State
import domain.entities.StateIndices

class StateCSVDataSource(
    private val file: File,
    private val csvHandler: CsvHandler
) {

    init {
        initStateFile()
    }

    fun getAllStates(): List<State> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file)
            .drop(StateIndices.HEADER_ROW)
            .mapNotNull { parts ->
                if (parts.size == 3) {
                    State(
                        id = parts[StateIndices.ID],
                        name = parts[StateIndices.NAME],
                        projectId = parts[StateIndices.PROJECT_ID]
                    )
                } else null
            }
    }

    fun editState(updatedState: State): Boolean {
        val allStates = getAllStates().toMutableList()
        val index = allStates.indexOfFirst { it.id == updatedState.id }

        return if (index != -1) {
            allStates[index] = updatedState
            writeStates(allStates)
            true
        } else {
            false
        }
    }

    fun existsState(stateId: String): Boolean {
        val allStates = getAllStates()
        return allStates.any { it.id == stateId }
    }

    fun deleteState(stateId: String): Boolean {
        val states = getAllStates().toMutableList()
        val removed = states.removeIf { it.id == stateId }

        if (removed) {
            writeStates(states)
            return true
        }
        return false
    }

    private fun writeStates(states: List<State>) {
        try {
            initStateFile()
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

    private fun initStateFile() {
        try {
            if (!file.exists()) file.createNewFile()

            csvHandler.writeHeaderIfNotExist(
                StateIndices.STATE_FILE_HEADER,
                file,
            )
        } catch (e: Exception) {
            println("Failed to initialize state file: ${e.message}")
        }
    }
}


