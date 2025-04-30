package data.dataSource

import domain.entities.StateIndices
import org.buinos.domain.entities.State
import java.io.File

class StateCSVDataSource(private val filePath: String) {

    private val file = File(filePath)

    fun getAllStates(): List<State> {
        if (!file.exists()) return emptyList()

        return file.readLines()
            .drop(1)
            .mapNotNull { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    State(
                        id = parts[StateIndices.ID],
                        name = parts[StateIndices.NAME],
                        projectId = parts[StateIndices.PROJECT_ID]
                    )
                } else null
            }
    }

    fun updateState(updatedState: State): Boolean {
        val states = getAllStates().toMutableList()
        val index = states.indexOfFirst { it.id == updatedState.id }

        return if (index != -1) {
            states[index] = updatedState
            saveAllStates(states)
            true
        } else {
            false
        }
    }

    private fun saveAllStates(states: List<State>) {
        val file = File(filePath)
        file.writeText("Id, Name, ProjectId\n")
        states.forEach {
            file.appendText("${it.id}, ${it.name}, ${it.projectId}\n")
        }
    }
}


