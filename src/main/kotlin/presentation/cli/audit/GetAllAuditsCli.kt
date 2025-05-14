package presentation.cli.audit

import domain.entities.Audit
import domain.usecases.audit.GetAllAuditUseCase
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

    suspend fun displayAllAudits() {
        displayAuditHeader()
        try {
            getAllAuditUseCase.execute().let { audits->
                audits.forEach { it.displayRow() }
            }
        }catch (e:Exception){
            uiController.printMessage("error from data source:${e.message}")
        }
    }

    companion object {
        const val HEADER =
            "ID || Entity ID || Entity Type || Action || Field || Old Value || New Value || User ID || Timestamp"
    }

}