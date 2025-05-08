package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import data.dummyData.DummyAudits
import data.dummyData.DummyAudits.DummyTaskAuditDto
import data.dummyData.DummyAudits.DummyTaskAuditRow
import data.dummyData.DummyAudits.dummyTaskAudit_CreateAction
import org.junit.jupiter.api.Test

class AuditDtoMapperTest {
    private val auditDtoMapper = AuditDtoMapper()


    @Test
    fun `should return Audit object when mapping toEntity`() {
        //when
        val expectedAudit = auditDtoMapper.toEntity(DummyTaskAuditDto)

        //then
        assertThat(expectedAudit).isEqualTo(DummyAudits.dummyTaskAudit_CreateAction)
    }


    @Test
    fun `should return dto object when mapping fromEntity`() {
        //when
        val expectedRow = auditDtoMapper.fromEntity(dummyTaskAudit_CreateAction)
        //then
        assertThat(expectedRow).isEqualTo(DummyTaskAuditDto)
    }


}