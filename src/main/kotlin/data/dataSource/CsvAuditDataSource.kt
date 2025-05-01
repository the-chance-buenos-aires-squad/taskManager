package data.dataSource

import data.dataSource.util.CsvHandler
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.io.File
import java.time.LocalDateTime


class CsvAuditDataSource(
    private val csvHandler: CsvHandler,
    private val file: File
) {


    fun addAudit(audit: Audit): Boolean {
        TODO()
    }


    fun getAllAudit(): List<Audit> {
        val rows = csvHandler.read(file).drop(1)
        return rows.map { row ->
            Audit(
                id = row[0],
                entityId = row[1],
                entityType = EntityType.valueOf(row[2]),
                action = ActionType.valueOf(row[3]),
                field = row[4].ifEmpty { null },
                oldValue = row[5].ifEmpty { null },
                newValue = row[6].ifEmpty { null },
                userId = row[7],
                timestamp = LocalDateTime.parse(row[8])
            )
        }
    }

}