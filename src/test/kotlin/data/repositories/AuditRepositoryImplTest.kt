package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.dummyData.DummyAudits
import data.repositories.mappers.AuditMapper
import data.dataSource.CsvAuditDataSource
import data.dataSource.dummyData.createDummyAudits
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditRepositoryImplTest {

  private val mockedDataSource = mockk<CsvAuditDataSource>(relaxed = true)
  private lateinit var auditRepositoryImpl: AuditRepositoryImpl
    private val mockedDataSource = mockk<CsvAuditDataSource>(relaxed = true)
    private lateinit var auditRepository: AuditRepositoryImpl
    private val mapper:AuditMapper = AuditMapper()


    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepositoryImpl(mockedDataSource,mapper)
    }



    @Test
    fun `should return empty list when no audits exist in data source`() {
    fun `should return true when add audit successful in the data source`(){
        //given
        every { mockedDataSource.getAllAudit() } returns emptyList()
        every { mockedDataSource.addAudit(mapper.mapEntityToRow(DummyAudits.dummyProjectAudit_CreateAction)) } returns true

        //when
        val result = auditRepositoryImpl.getAllAudit()
        val result = auditRepository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        //then
        assertThat(result).isEmpty()
        assertThat(result).isTrue()
    }

    @Test
    fun `should return all audits from data source`() {
    fun `should return false when add audit unSuccessful in the data source`(){
        //given
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyProjectCreateAction,
            createDummyAudits.dummyUserUpdateAction
        )
        every { mockedDataSource.getAllAudit() } returns expectedAudits
        every { mockedDataSource.addAudit(mapper.mapEntityToRow(DummyAudits.dummyProjectAudit_CreateAction)) } returns false

        //when
        val result = auditRepository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        val result = auditRepositoryImpl.getAllAudit()
        //then
        assertThat(result).isFalse()
        assertThat(result).isEqualTo(expectedAudits)
    }





}