package data.dataSource.audit

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.result.InsertOneResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.reactivestreams.client.FindPublisher
import data.dto.AuditDto
import data.dummyData.DummyAudits.DummyTaskAuditDto
import di.MongoCollections
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MongoAuditDataSourceTest{

    private lateinit var  dataSource : MongoAuditDataSource
    private  var  auditCollection: MongoCollection<AuditDto> = mockk(relaxed = true)
    private  var testAudit: AuditDto = DummyTaskAuditDto

    @BeforeEach
    fun setUp(){
        dataSource = MongoAuditDataSource(auditCollection)
    }


    @Test
    fun `addAudit should call collection insertOne`() = runTest {
        // Arrange
        val mockResult = mockk<InsertOneResult>()
        every { mockResult.wasAcknowledged() } returns true

        coEvery { auditCollection.insertOne(document = testAudit, options = any()) } returns mockResult

        // Act
        val result = dataSource.addAudit(testAudit)

        // Assert
        coVerify { auditCollection.insertOne(testAudit,any()) }
        coEvery { auditCollection.insertOne(testAudit,any()).wasAcknowledged() }
        assertThat(result).isTrue()
    }


    @Test
    fun `getAllAudit should return all Audit`() = runTest {
        val expectedAudits = listOf(testAudit, testAudit)


        val mockFindFlow = mockk<FindFlow<AuditDto>>(relaxed = true)
        coEvery { mockFindFlow.toList() } returns expectedAudits

        coEvery { auditCollection.find() } returns mockFindFlow
        coEvery { auditCollection.find().toList() } returns expectedAudits


        val result = dataSource.getAllAudit()


        coVerify { auditCollection.find() }
    }

}