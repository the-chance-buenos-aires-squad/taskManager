package data.dataSource

import data.dataSource.util.CsvHandler
import domain.entities.Audit
import java.io.File



class CsvAuditDataSource (
    private val csvHandler: CsvHandler,
    private val file:File
){



     fun addAudit():Boolean{
         TODO()
     }


    fun getAllAudit():List<Audit>{
        TODO()
    }



}