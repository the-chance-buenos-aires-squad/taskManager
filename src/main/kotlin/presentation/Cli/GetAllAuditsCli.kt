package presentation.Cli

import domain.entities.Audit
import domain.usecases.GetAllAuditUseCase

class GetAllAuditsCli(private val getAllAuditUseCase: GetAllAuditUseCase) {
    fun displayAuditHeader() {
        val header = "ID || Entity ID || Entity Type || Action || Field || Old Value || New Value || User ID || Timestamp"
        println(header)
        println("-".repeat(header.length))
    }

    fun Audit.displayRow() {
        println(
            "${this.id}||${this.entityId}||${this.entityType}|| ${this.action}||${this.field}||" +
                    "${this.oldValue}||${this.newValue}||${this.userId}||${this.timestamp}"
        )
    }

    fun displayAllAudits() {
        displayAuditHeader()
        val audits = getAllAuditUseCase.getAllAudit()
        audits.forEach { it.displayRow() }
    }

    fun displaySingleAudit(audit: Audit) {
        displayAuditHeader()
        audit.displayRow()
    }

}