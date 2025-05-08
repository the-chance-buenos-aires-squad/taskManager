package data.dataSource.audit

import com.google.common.truth.Truth.assertThat
import data.dto.AuditDto
import data.dummyData.DummyAudits.DummyTaskAuditDto
import data.dummyData.DummyAudits.DummyTaskAuditRow
import org.junit.jupiter.api.Test

class AuditDtoParserTest{
    private val dto = DummyTaskAuditDto
    private val row:List<String> = DummyTaskAuditRow
    private val auditDtoParser = AuditDtoParser()

    @Test
    fun `mapping to dto should return dto object with the same properties values`(){
        //when
        val result: AuditDto = auditDtoParser.toDto(row)

        assertThat(result).isEqualTo(dto)
    }

    @Test
    fun `mapping from dto should return row  with the same values`(){
        //when
        val result: List<String> = auditDtoParser.fromDto(dto)

        assertThat(result).isEqualTo(row)
    }


}