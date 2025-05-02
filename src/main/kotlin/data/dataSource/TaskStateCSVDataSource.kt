package data.dataSource
import java.io.File
import data.util.CsvHandler
import domain.entities.State

class TaskStateCSVDataSource(
    private val file: File,
    private val csvHandler: CsvHandler
): TaskStateDataSource {

    override fun createTaskState(state: State): Boolean {
        TODO("Not yet implemented")
    }

    override fun editTaskState(state: State): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteTaskState(stateId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllTaskStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun existsTaskState(stateId: String): Boolean {
        TODO("Not yet implemented")
    }

}


