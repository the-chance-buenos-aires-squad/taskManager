package presentation.Cli

import domain.entities.Audit
import domain.usecases.GetAllAuditUseCase
import presentation.UiController

class GetAllAuditsCli(
    private val getAllAuditUseCase: GetAllAuditUseCase,
    private val uiController: UiController
) {
    private fun displayAuditHeader() {
        uiController.printMessage(HEADER)
        uiController.printMessage("-".repeat(HEADER.length))
    }

    private fun Audit.displayRow() {
        uiController.printMessage(
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

    companion object {
        const val HEADER =
            "ID || Entity ID || Entity Type || Action || Field || Old Value || New Value || User ID || Timestamp"
    }

}