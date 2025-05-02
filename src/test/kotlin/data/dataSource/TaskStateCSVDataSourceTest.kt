package data.dataSource

import data.util.CsvHandler
import org.junit.jupiter.api.BeforeEach
import java.io.File

class TaskStateCSVDataSourceTest {

    private var testStateFile: File = File("test_states.csv")
    private lateinit var csvHandler: CsvHandler
    private lateinit var taskStateCSVDataSource: TaskStateCSVDataSource

    @BeforeEach
    fun setup() {
        taskStateCSVDataSource = TaskStateCSVDataSource(testStateFile, csvHandler)
    }

}