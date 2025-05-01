package data.dataSource

import data.dataSource.util.CsvHandler
import java.io.File



class CsvAuditDataSource (
    private val csvHandler: CsvHandler,
    private val file:File
){
}