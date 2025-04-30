package data.dataSource

import org.buinos.domain.entities.State
import java.io.File

class StateCSVDataSource(private val filePath: String = "kotlin/data/resource/state.csv") {

    private val file = File(filePath)

    fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    fun updateState(updatedState: State): Boolean {
        TODO("Not yet implemented")
    }
}


