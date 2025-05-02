package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.dataSource.dummyData.createDummyAudits
import data.dataSource.dummyData.createDummyAudits.dummyProjectCreateAction
import data.dataSource.dummyData.createDummyAudits.dummyTaskCreateAction
import data.dummyData.DummyAudits
import data.repositories.mappers.AuditMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditRepositoryImplTest {

    private val mockedDataSource = mockk<CsvAuditDataSource>(relaxed = true)
    private lateinit var auditRepository: AuditRepositoryImpl
    private val mapper: AuditMapper = AuditMapper()


    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepositoryImpl(mockedDataSource, mapper)
    }


    @Test
    fun `should return empty list when no audits exist in data source`() {
        every { mockedDataSource.getAllAudit() } returns emptyList()

        val result = auditRepository.getAllAudit()
        assertThat(result).isEmpty()

    }

    @Test
    fun `should return true when add audit successful in the data source`() {
        //given
        every { mockedDataSource.addAudit(mapper.mapEntityToRow(DummyAudits.dummyProjectAudit_CreateAction)) } returns true

        //when
        val result = auditRepository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return all audits from data source`() {
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateActionRow,
            createDummyAudits.dummyProjectCreateActionRow,
        )
        every { mockedDataSource.getAllAudit() } returns expectedAudits

        val result = auditRepository.getAllAudit()
        assertThat(result).isEqualTo(listOf(dummyTaskCreateAction, dummyProjectCreateAction))
    }

    @Test
    fun `should return false when add audit unSuccessful in the data source`() {
        //given
        every { mockedDataSource.addAudit(mapper.mapEntityToRow(DummyAudits.dummyProjectAudit_CreateAction)) } returns false

        //when
        val result = auditRepository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        //then
        assertThat(result).isFalse()
    }


}