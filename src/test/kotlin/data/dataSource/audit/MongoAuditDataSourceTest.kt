package data.dataSource.audit

import com.google.common.truth.Truth.assertThat
import data.dto.AuditDto
import data.dummyData.DummyAudits.DummyTaskAuditDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MongoAuditDataSourceTest{

    private  var  dataSource : FakeMongoAuditDataSource= FakeMongoAuditDataSource()
    private  var testAudit: AuditDto = DummyTaskAuditDto

    @Test
    fun `addAudit should return true`() = runTest {
        val added = dataSource.addAudit(testAudit)
        assertThat(added).isTrue()
    }

    @Test
    fun `getAllAudit should return all Audit`() = runTest {
        dataSource.addAudit(testAudit)
        dataSource.addAudit(testAudit)

        val result = dataSource.getAllAudit()
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(testAudit, testAudit)
    }


}