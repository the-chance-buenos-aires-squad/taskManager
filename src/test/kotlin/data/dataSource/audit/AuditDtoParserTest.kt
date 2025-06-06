package data.dataSource.audit

import com.google.common.truth.Truth.assertThat
import data.dto.AuditDto
import data.dummyData.DummyAudits.DummyTaskAuditDto
import data.dummyData.DummyAudits.DummyTaskAuditRow
import org.junit.jupiter.api.Test

class AuditDtoParserTest {
    private val dto = DummyTaskAuditDto
    private val row: List<String> = DummyTaskAuditRow
    private val rowWithUnknownValues = row.toMutableList().also {
        it[ENTITY_TYPE_ROW] = "unknown type"
        it[ACTION_ROW] = "unknown action"
    }.toList()
    private val auditDtoParser = AuditDtoParser()

    @Test
    fun `parsing to dto should return dto object with the same properties values`() {
        //when
        val result: AuditDto = auditDtoParser.fromType(row)

        assertThat(result).isEqualTo(dto)
    }

    @Test
    fun `parsing from dto should return row  with the same values`() {
        //when
        val result: List<String> = auditDtoParser.toType(dto)

        assertThat(result).isEqualTo(row)
    }


    @Test
    fun `parsing to dto with unKnown entityType and action should should take default null value`() {
        //when
        val resultDto: AuditDto = auditDtoParser.fromType(rowWithUnknownValues)

        assertThat(resultDto.entityType).isEqualTo("unknown type")
        assertThat(resultDto.action).isEqualTo("unknown action")
    }

    @Test
    fun `parsing from dto with null properties should be empty value in row`() {
        val dtoWithNullValues =
            dto.copy(entityType = null, action = null, field = null, oldValue = null, newValue = null)
        val resultRow = auditDtoParser.toType(dtoWithNullValues)

        assertThat(resultRow[ENTITY_TYPE_ROW]).isEmpty()
        assertThat(resultRow[ACTION_ROW]).isEmpty()
        assertThat(resultRow[FIELD_ROW]).isEmpty()
        assertThat(resultRow[OLD_VALUE_ROW]).isEmpty()
        assertThat(resultRow[NEW_VALUE_ROW]).isEmpty()
    }


    private companion object {
        private const val ENTITY_TYPE_ROW = 2
        private const val ACTION_ROW = 3
        private const val FIELD_ROW = 4
        private const val OLD_VALUE_ROW = 5
        private const val NEW_VALUE_ROW = 6
    }

}