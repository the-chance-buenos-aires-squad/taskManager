package data.dataSource

import java.io.File

class StateCSVDataSource(private val filePath: String = "kotlin/data/resource/state.csv") {

    private val file = File(filePath)
}


