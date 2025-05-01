package data.dataSource

import data.dataSource.util.CsvHandler
import domain.entities.Audit
import java.io.File



class CsvAuditDataSource (
    private val csvHandler: CsvHandler,
    private val file:File
){



     fun addAudit(audit: Audit):Boolean{
       return  try {
             csvHandler.write(
                 row = audit.getRow(),
                 file = file,
                 append = true
             )
             true
         }catch (e:Exception){
             false
         }
     }


    fun getAllAudit():List<Audit>{
        TODO()
    }


    private fun Audit.getRow():List<String>{
        return listOf(
            this.id,
            this.entityId,
            this.entityType.name,
            this.action.name,
            this.field?:"",
            this.oldValue?:"",
            this.newValue?:"",
            this.userId,
            this.timestamp.toString()
        )
    }


}