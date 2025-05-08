package data.dataSource.audit

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.AuditDto
import di.MongoCollections
import kotlinx.coroutines.flow.toList

class MongoAuditDataSource(
    private val auditCollection: MongoCollection<AuditDto>
):AuditDataSource {
    override suspend fun addAudit(auditDto: AuditDto): Boolean {
       return auditCollection.insertOne(auditDto).wasAcknowledged()
    }

    override suspend fun getAllAudit(): List<AuditDto> {
        return auditCollection.find().toList()
    }
}