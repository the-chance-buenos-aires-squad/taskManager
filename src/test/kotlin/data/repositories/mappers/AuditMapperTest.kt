package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import data.dummyData.DummyAudits
import data.dummyData.DummyAudits.DummyTaskAuditRow
import org.junit.jupiter.api.Test

class AuditMapperTest {
    private val auditMapper = AuditMapper()


    @Test
    fun `should return Audit object when Calling mapRowToEntity`() {
        //when
        val expectedAudit = auditMapper.mapRowToEntity(DummyTaskAuditRow)

        //then
        assertThat(expectedAudit).isEqualTo(DummyAudits.dummyTaskAudit_CreateAction)
    }


    @Test
    fun `should return row string when calling mapEntityToRow`() {
        //when
        val expectedRow = auditMapper.mapEntityToRow(DummyAudits.dummyTaskAudit_CreateAction)
        //then
        assertThat(expectedRow).isEqualTo(DummyTaskAuditRow)
    }


}